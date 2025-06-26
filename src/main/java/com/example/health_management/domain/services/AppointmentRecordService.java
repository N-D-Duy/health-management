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
import com.example.health_management.infrastructure.client.MailClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
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
    private final MailClient mailClient;

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

            appointmentRecord.setDoctor(doctor);
            appointmentRecord.setUser(user);
            appointmentRecord.setHealthProvider(healthProvider);


            DoctorScheduleDTO doctorScheduleDTO = DoctorScheduleDTO.builder()
                    .doctorId(request.getDoctorId())
                    .startTime(request.getScheduledAt())
                    .patientName(user.getFirstName() + " " + user.getLastName())
                    .appointmentStatus(AppointmentStatus.SCHEDULED)
                    .examinationType("-")
                    .note(request.getNote())
                    .appointmentRecord(appointmentRecord)
                    .build();


            //check if there is any deposit been holding
            AppointmentRecord latestHeldDeposit = appointmentRecordRepository
                    .findLatestHeldDepositByUserId(user.getId())
                    .orElse(null);
            if (latestHeldDeposit != null) {
                if (latestHeldDeposit.getDepositStatus() == DepositStatus.HOLD) {
                    // If there is a held deposit, update the appointment record to use this deposit
                    latestHeldDeposit.setDepositStatus(DepositStatus.USED);
                    appointmentRecord.setStatus(AppointmentStatus.SCHEDULED);
                    appointmentRecordRepository.save(latestHeldDeposit);
                    log.info("Deposit status updated to USED");
                }
            }
            AppointmentRecord savedRecord = appointmentRecordRepository.save(appointmentRecord);
            doctorScheduleService.createDoctorSchedule(doctorScheduleDTO);

            AppointmentRecordDTO result = appointmentRecordMapper.toDTO(savedRecord);
            appointmentCacheService.invalidateAppointmentCaches(savedRecord.getId(), user.getId(), doctor.getId());
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

    private String getAvatarDir(Long uid) {
        try {
            String avatar = "src/main/resources/static/avatars/user_" + uid + ".jpg";
            Path imagePath = Paths.get(avatar);
            byte[] imageBytes = Files.readAllBytes(imagePath);
            String base64 = Base64.getEncoder().encodeToString(imageBytes);
            return "data:image/jpeg;base64," + base64;
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            data.put("userAvatar", getAvatarDir(appointmentRecord.getUser().getId()));
            data.put("doctor", appointmentRecord.getDoctor());
            data.put("note", appointmentRecord.getNote());

            PrescriptionDTO prescription = appointmentRecord.getPrescription();
            data.put("prescription", prescription);

            data.put("medicalHistories", prescription != null && prescription.getMedicalConditions() != null ? prescription.getMedicalConditions() : List.of());
            data.put("medications", prescription != null && prescription.getDetails() != null ? prescription.getDetails() : List.of());

            String fileName = "Appointment" + "_" + appointmentRecord.getUser().getFirstName() + ".pdf";

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

    /*
    * string 1: email
    * string 2: content to send to this email
    * */
    private void sendCancellationNotification(Map<String, String> notificationData) {
        if(notificationData == null || notificationData.isEmpty()) {
            throw new ConflictException("Notification data cannot be null or empty");
        }
        notificationData.forEach((key, value) -> {
            if(key == null || key.isEmpty() || value == null || value.isEmpty()) {
                throw new ConflictException("Email and content cannot be null or empty");
            }
            try {
                mailClient.sendMail(key, value).subscribe();
                log.info("Cancellation notification sent to: {}", key);
            } catch (Exception e) {
                log.error("Failed to send cancellation notification to {}: {}", key, e.getMessage());
                throw new ConflictException("Failed to send cancellation notification: " + e.getMessage());
            }
        });
    }

    private void notifyCancellation(AppointmentRecord appointment, boolean isCancelledByDoctor, String message) {
        String email = isCancelledByDoctor
                ? appointment.getUser().getAccount().getEmail()
                : appointment.getDoctor().getUser().getAccount().getEmail();

        String content = isCancelledByDoctor
                ? "[Doctor Cancelled] " + message
                : "[Patient Cancelled] " + message;

        sendCancellationNotification(Map.of(email, content));
    }


    private String patientCancelAppointment(AppointmentRecord appointment) {
        try {
            String result;
            String mailContent;
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createdAt = appointment.getCreatedAt();
            LocalDateTime scheduledAt = appointment.getScheduledAt();

            if (Duration.between(createdAt, now).toMinutes() <= 15) {
                appointment.setDepositStatus(DepositStatus.FULL_REFUND_PENDING);
                result = "Appointment cancelled successfully, full deposit refunded to you";
                mailContent = "Patient has cancelled the appointment. Full deposit will be refunded to them.";
            } else if (scheduledAt.isAfter(now.plusHours(12))) {
                appointment.setDepositStatus(DepositStatus.PARTIAL_REFUND_PENDING);
                result = "Appointment cancelled, 50% of your deposit is lost based on policy";
                mailContent = "Patient has cancelled the appointment. They will lose 50% of the deposit.";
            } else {
                appointment.setDepositStatus(DepositStatus.LOST);
                result = "Appointment cancelled, you lost full deposit based on policy";
                mailContent = "Patient has cancelled the appointment. Full deposit is lost.";
            }

            setAppointmentStatusCancel(appointment);
            doctorScheduleService.updateDoctorScheduleStatusByAppointmentId(
                    appointment.getId(), AppointmentStatus.CANCELLED);

            appointmentRecordRepository.save(appointment);
            appointmentCacheService.invalidateAppointmentCaches(
                    appointment.getId(), appointment.getUser().getId(), appointment.getDoctor().getId());

            notifyCancellation(appointment, false, mailContent);
            return result; // show to patient
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
            String result = "Appointment cancelled by doctor, deposit is on hold";
            String mailContent = "Doctor has cancelled the appointment. Deposit is on hold until further action. You can choose to refund or reschedule later. After 24 hours, the deposit will be refunded automatically.";
            notifyCancellation(appointment, true, mailContent);
            return result;
        } catch (Exception e) {
            log.error("Error cancelling appointment for doctor: {}", e.getMessage());
            throw new ConflictException("Error cancelling appointment: " + e.getMessage());
        }
    }

    private void setAppointmentStatusCancel(AppointmentRecord appointment) {
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }

    public AppointmentRecord getAppointmentById(Long id) {
        return appointmentRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment Record not found with ID: " + id));
    }

    public boolean isAppointmentExists(Long appointmentId) {
        return appointmentRecordRepository.existsById(appointmentId);
    }

    public void updateStatus(Long appointmentId, AppointmentStatus status) {
        AppointmentRecord appointmentRecord = getAppointmentById(appointmentId);
        appointmentRecord.setStatus(status);
        appointmentRecordRepository.save(appointmentRecord);
        appointmentCacheService.invalidateAppointmentCaches(appointmentId, appointmentRecord.getUser().getId(), appointmentRecord.getDoctor().getId());
    }

    public void updateDepositStatus(Long appointmentId, DepositStatus depositStatus) {
        AppointmentRecord appointmentRecord = getAppointmentById(appointmentId);
        appointmentRecord.setDepositStatus(depositStatus);
        appointmentRecordRepository.save(appointmentRecord);
        appointmentCacheService.invalidateAppointmentCaches(appointmentId, appointmentRecord.getUser().getId(), appointmentRecord.getDoctor().getId());
    }
}
