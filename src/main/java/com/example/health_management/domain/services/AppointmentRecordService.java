package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.appointment_record.request.AppointmentRecordRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.request.UpdateAppointmentRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.response.AppointmentRecordDTO;
import com.example.health_management.application.DTOs.doctor.DoctorScheduleDTO;
import com.example.health_management.application.DTOs.prescription.PrescriptionDTO;
import com.example.health_management.application.mapper.AppointmentRecordMapper;
import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.enums.DepositStatus;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.cache.services.AppointmentCacheService;
import com.example.health_management.domain.entities.AppointmentRecord;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.HealthProvider;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.*;
import com.example.health_management.domain.services.exporters.PDFExporter;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AppointmentRecordService {
    private final AppointmentRecordRepository appointmentRecordRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final HealthProviderRepository healthProviderRepository;
    private final AppointmentRecordMapper appointmentRecordMapper;
    private final PrescriptionService prescriptionService;
    private final DoctorScheduleService doctorScheduleService;
    private final AppointmentCacheService appointmentCacheService;
    private final AccountRepository accountRepository;

    public AppointmentRecordDTO create(AppointmentRecordRequestDTO request) {
        try {
            Doctor doctor = doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + request.getDoctorId()));

            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.getUserId()));

            HealthProvider healthProvider = healthProviderRepository.findById(request.getHealthProviderId())
                    .orElseThrow(() -> new EntityNotFoundException("Health Provider not found with ID: " + request.getHealthProviderId()));

            AppointmentRecord appointmentRecord = new AppointmentRecord();
            appointmentRecord.setNote(request.getNote());
            appointmentRecord.setAppointmentType(request.getAppointmentType());
            appointmentRecord.setScheduledAt(request.getScheduledAt());
            appointmentRecord.setStatus(request.getStatus());
            appointmentRecord.setPaymentStatus(request.getPaymentStatus());

            appointmentRecord.setDoctor(doctor);
            appointmentRecord.setUser(user);
            appointmentRecord.setHealthProvider(healthProvider);

            AppointmentRecord savedRecord = appointmentRecordRepository.save(appointmentRecord);

            DoctorScheduleDTO doctorScheduleDTO = DoctorScheduleDTO.builder()
                    .doctorId(request.getDoctorId())
                    .startTime(request.getScheduledAt())
                    .patientName(user.getFirstName() + " " + user.getLastName())
                    .appointmentStatus(AppointmentStatus.SCHEDULED)
                    .examinationType("-")
                    .note(request.getNote())
                    .appointmentRecord(appointmentRecord)
                    .build();

            doctorScheduleService.createDoctorSchedule(doctorScheduleDTO);

            AppointmentRecordDTO result = appointmentRecordMapper.toDTO(savedRecord);
            appointmentCacheService.invalidateAppointmentCaches(savedRecord.getId(), user.getId(), doctor.getId());

            //check if there is any deposit been holding
            AppointmentRecord latestHeldDeposit = appointmentRecordRepository
                    .findLatestHeldDepositByUserId(user.getId())
                    .orElse(null);
            if (latestHeldDeposit != null) {
                if (latestHeldDeposit.getDepositStatus() == DepositStatus.HOLD) {
                    // If there is a held deposit, update the appointment record to use this deposit
                    latestHeldDeposit.setDepositStatus(DepositStatus.USED);
                    appointmentRecordRepository.save(latestHeldDeposit);
                    log.info("Deposit status updated to USED");
                }
            }

            return result;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ConflictException("Error creating appointment record: " + e.getMessage());
        }
    }

    public AppointmentRecordDTO update(UpdateAppointmentRequestDTO request) {
        try {
            AppointmentRecord appointmentRecord = appointmentRecordRepository
                    .findById(request.getId())
                    .orElseThrow(() -> new ConflictException("AppointmentRecord not found"));

            appointmentRecordMapper.update(appointmentRecord, request);

            prescriptionService.updatePrescription(appointmentRecord, request);

            updateRelationships(appointmentRecord, request);

            appointmentRecordRepository.save(appointmentRecord);

            AppointmentRecordDTO updatedAppointment = appointmentRecordMapper.toDTO(appointmentRecord);
            Long userId = appointmentRecord.getUser().getId();
            Long doctorId = appointmentRecord.getDoctor().getId();

            appointmentCacheService.invalidateAppointmentCaches(request.getId(), userId, doctorId);
            appointmentCacheService.cacheAppointment(request.getId(), updatedAppointment);

            return updatedAppointment;
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    private void updateRelationships(@NonNull AppointmentRecord appointmentRecord, @NonNull UpdateAppointmentRequestDTO request) {
        if (request.getDoctorId() != null) {
            appointmentRecord.setDoctor(doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + request.getDoctorId())));
        }

        if (request.getUserId() != null) {
            appointmentRecord.setUser(userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.getUserId())));
        }

        if (request.getHealthProviderId() != null) {
            appointmentRecord.setHealthProvider(healthProviderRepository.findById(request.getHealthProviderId())
                    .orElseThrow(() -> new EntityNotFoundException("Health Provider not found with ID: " + request.getHealthProviderId())));
        }
    }

    public String deleteAppointmentRecord(Long appointmentRecordId) {
        try {
            AppointmentRecord appointmentRecord = appointmentRecordRepository.findByIdActive(appointmentRecordId);
            if (appointmentRecord == null) {
                throw new ConflictException("Appointment Record not found with ID: " + appointmentRecordId);
            }

            Long userId = appointmentRecord.getUser().getId();
            Long doctorId = appointmentRecord.getDoctor().getId();
            appointmentRecord.setStatus(AppointmentStatus.CANCELLED);
            appointmentRecordRepository.deleteById(appointmentRecordId);

            doctorScheduleService.updateDoctorScheduleStatusByAppointmentId(appointmentRecordId, AppointmentStatus.CANCELLED);

            appointmentCacheService.invalidateAppointmentCaches(appointmentRecordId, userId, doctorId);

            return "Appointment Record deleted successfully";
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ConflictException(e.getMessage());
        }
    }

    public List<AppointmentRecordDTO> findAll() {
        try {
            List<AppointmentRecordDTO> cachedAppointments = appointmentCacheService.getCachedAllAppointments();
            if (cachedAppointments != null) {
                return cachedAppointments;
            }
            List<AppointmentRecord> appointmentRecords = appointmentRecordRepository.findAll();
            List<AppointmentRecordDTO> result = appointmentRecords.stream().map(appointmentRecordMapper::toDTO).toList();

            if(!result.isEmpty()){
                appointmentCacheService.cacheAllAppointments(result);
            }
            return result;
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public AppointmentRecordDTO getById(Long id) {
        try {
            AppointmentRecordDTO cachedAppointment = appointmentCacheService.getCachedAppointment(id);
            if (cachedAppointment != null) {
                return cachedAppointment;
            }

            AppointmentRecord appointmentRecord = appointmentRecordRepository.findById(id).orElse(null);
            if (appointmentRecord == null) {
                throw new ConflictException("Appointment Record not found with ID: " + id);
            }

            AppointmentRecordDTO result = appointmentRecordMapper.toDTO(appointmentRecord);

            if(result!=null){
                appointmentCacheService.cacheAppointment(id, result);
            }

            return result;
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public List<AppointmentRecordDTO> getByUserId(Long userId) {
        try {

            List<AppointmentRecordDTO> cachedAppointments = appointmentCacheService.getCachedUserAppointments(userId);
            if (cachedAppointments != null) {
                return cachedAppointments;
            }

            List<AppointmentRecord> appointmentRecords = appointmentRecordRepository.findByUserId(userId);
            List<AppointmentRecordDTO> result = appointmentRecords.stream().map(appointmentRecordMapper::toDTO).toList();

            if(!result.isEmpty()){
                appointmentCacheService.cacheUserAppointments(userId, result);
            }
            return result;
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public List<AppointmentRecordDTO> getByDoctorId(Long doctorId) {
        try {

            List<AppointmentRecordDTO> cachedAppointments = appointmentCacheService.getCachedDoctorAppointments(doctorId);
            if (cachedAppointments != null) {
                return cachedAppointments;
            }

            List<AppointmentRecord> appointmentRecords = appointmentRecordRepository.findByDoctorId(doctorId);
            List<AppointmentRecordDTO> result = appointmentRecords.stream().map(appointmentRecordMapper::toDTO).toList();

            if(!result.isEmpty()){
                appointmentCacheService.cacheDoctorAppointments(doctorId, result);
            }
            return result;
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public ByteArrayResource exportAppointmentPDF(Long appointmentRecordId, String language) {
        try{
            if(language == null || language.isEmpty()) {
                language = "en";
            }
            if(!language.equalsIgnoreCase("en") && !language.equalsIgnoreCase("vie")) {
                throw new ConflictException("Unsupported language: " + language);
            }
            AppointmentRecordDTO appointmentRecord = getById(appointmentRecordId);
            if (appointmentRecord == null) {
                throw new ConflictException("Appointment Record not found with ID: " + appointmentRecordId);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("scheduled_at", appointmentRecord.getScheduledAt());
            data.put("user", appointmentRecord.getUser());
            data.put("doctor", appointmentRecord.getDoctor());
            data.put("note", appointmentRecord.getNote());

            PrescriptionDTO prescription = appointmentRecord.getPrescription();
            data.put("prescription", prescription);

            data.put("medicalHistories", appointmentRecord.getPrescription().getMedicalConditions());

            data.put("medications", prescription != null ? prescription.getDetails() : List.of());

            String fileName = "Doctor_Schedule_" + appointmentRecord.getDoctor().getFirstName() + ".pdf";

            String template = language.equalsIgnoreCase("en") ? "template_appointment_en" : "template_appointment_vie";
            return new PDFExporter().generatePdfFromTemplate(template, data, fileName);

        } catch (Exception e) {
            log.error("Error exporting appointment PDF: {}", e.getMessage());
            throw new ConflictException("Error exporting appointment PDF: " + e.getMessage());
        }
    }

    public String cancelAppointment(Long userId, Long appointmentId) {
        AppointmentRecord appointment = appointmentRecordRepository.findById(appointmentId)
                .filter(a -> a.getUser().getId().equals(userId) || a.getDoctor().getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Appointment not found or user mismatch"));
        if(appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ConflictException("Appointment already cancelled");
        }

        if(appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ConflictException("Cannot cancel a completed appointment");
        }

        if(appointment.getScheduledAt().isBefore(LocalDateTime.now())) {
            throw new ConflictException("Cannot cancel an appointment that has already passed");
        }

        if(accountRepository.isPatient(userId)){
            return patientCancelAppointment(appointment);
        }
        else if(accountRepository.isDoctor(userId)){
            return doctorCancelAppointment(appointment);
        } else {
            throw new ConflictException("User is neither a doctor nor a patient");
        }
    }

    private String patientCancelAppointment(AppointmentRecord appointment) {
        try {
            String result;
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createdAt = appointment.getCreatedAt();
            LocalDateTime scheduledAt = appointment.getScheduledAt();

            if (Duration.between(createdAt, now).toMinutes() <= 15) {
                // Vừa tạo trong vòng 15 phút → cho hủy, hoàn tiền
                appointment.setDepositStatus(DepositStatus.REFUNDED);
                result = "Appointment cancelled successfully, based on our policy, you will receive a full refund of your deposit";
            } else if (scheduledAt.isAfter(now.plusHours(12))) {
                // Lịch còn xa hơn 12h → mất 50% tiền cọc
                appointment.setDepositStatus(DepositStatus.PARTIAL_LOST);
                result = "Appointment cancelled successfully, based on our policy, you lost 50% of your deposit";
            } else {
                // Lịch sắp tới → mất toàn bộ tiền cọc
                appointment.setDepositStatus(DepositStatus.LOST);
                result = "Appointment cancelled successfully, base on our policy, you lost your deposit";
            }

            setAppointmentStatusCancel(appointment);
            doctorScheduleService.updateDoctorScheduleStatusByAppointmentId(
                    appointment.getId(),
                    AppointmentStatus.CANCELLED
            );

            appointmentRecordRepository.save(appointment);
            appointmentCacheService.invalidateAppointmentCaches(
                    appointment.getId(),
                    appointment.getUser().getId(),
                    appointment.getDoctor().getId()
            );

            return result;
        } catch (Exception e) {
            log.error("Error cancelling appointment for patient: {}", e.getMessage());
            throw new ConflictException("Error cancelling appointment: " + e.getMessage());
        }
    }

    private String doctorCancelAppointment(AppointmentRecord appointment) {
        try {
            // Bác sĩ hủy: tạm giữ cọc (user sẽ chọn đổi hoặc hoàn cọc sau)
            appointment.setDepositStatus(DepositStatus.HOLD);

            setAppointmentStatusCancel(appointment);

            doctorScheduleService.updateDoctorScheduleStatusByAppointmentId(
                    appointment.getId(),
                    AppointmentStatus.CANCELLED
            );

            appointmentRecordRepository.save(appointment);

            appointmentCacheService.invalidateAppointmentCaches(
                    appointment.getId(),
                    appointment.getUser().getId(),
                    appointment.getDoctor().getId()
            );

            return "Appointment cancelled by doctor, deposit is on hold";
        } catch (Exception e) {
            log.error("Error cancelling appointment for doctor: {}", e.getMessage());
            throw new ConflictException("Error cancelling appointment: " + e.getMessage());
        }
    }


    private void setAppointmentStatusCancel(AppointmentRecord appointment) {
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }
}
