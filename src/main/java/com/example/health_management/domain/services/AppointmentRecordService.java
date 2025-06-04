package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.appointment_record.request.AppointmentRecordRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.request.UpdateAppointmentRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.response.AppointmentRecordDTO;
import com.example.health_management.application.DTOs.doctor.DoctorScheduleDTO;
import com.example.health_management.application.mapper.AppointmentRecordMapper;
import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.cache.services.AppointmentCacheService;
import com.example.health_management.domain.entities.AppointmentRecord;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.HealthProvider;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AppointmentRecordRepository;
import com.example.health_management.domain.repositories.DoctorRepository;
import com.example.health_management.domain.repositories.HealthProviderRepository;
import com.example.health_management.domain.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
                    .appointmentStatus(request.getStatus().toString())
                    .examinationType("-")
                    .note(request.getNote())
                    .build();

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

            doctorScheduleService.updateDoctorScheduleStatus(appointmentRecordId, AppointmentStatus.CANCELLED.toString());

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
}
