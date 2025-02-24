package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.appointment_record.request.AppointmentRecordRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.request.UpdateAppointmentRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.response.AppointmentRecordDTO;
import com.example.health_management.application.DTOs.doctor.DoctorScheduleDTO;
import com.example.health_management.application.DTOs.logging.LoggingDTO;
import com.example.health_management.application.mapper.AppointmentRecordMapper;
import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.enums.LoggingType;
import com.example.health_management.common.shared.exceptions.ConflictException;
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
    private final LoggingService loggingService;
    private final DoctorScheduleService doctorScheduleService;


    public AppointmentRecordDTO create(AppointmentRecordRequestDTO request){
        try {
            // Find entities first
            Doctor doctor = doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + request.getDoctorId()));

            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.getUserId()));

            HealthProvider healthProvider = healthProviderRepository.findById(request.getHealthProviderId())
                    .orElseThrow(() -> new EntityNotFoundException("Health Provider not found with ID: " + request.getHealthProviderId()));

            // Create and populate appointment record
            AppointmentRecord appointmentRecord = new AppointmentRecord();
            appointmentRecord.setNote(request.getNote());
            appointmentRecord.setAppointmentType(request.getAppointmentType());
            appointmentRecord.setScheduledAt(request.getScheduledAt());
            appointmentRecord.setStatus(request.getStatus());

            // Set relationships
            appointmentRecord.setDoctor(doctor);
            appointmentRecord.setUser(user);
            appointmentRecord.setHealthProvider(healthProvider);

            // Save and return
            AppointmentRecord savedRecord = appointmentRecordRepository.save(appointmentRecord);

            DoctorScheduleDTO doctorScheduleDTO = DoctorScheduleDTO.builder()
                    .doctorId(request.getDoctorId())
                    .startTime(request.getScheduledAt())
                    .endTime(request.getScheduledAt().plusMinutes(60))
                    .currentPatientCount(1)
                    .build();
            //create doctor schedule
            doctorScheduleService.updateOrCreateDoctorSchedule(doctorScheduleDTO, true);

            loggingService.saveLog(LoggingDTO.builder().message("Appointment record with id " + savedRecord.getId() + " created").type(LoggingType.APPOINTMENT_CREATED).build());
            return appointmentRecordMapper.toDTO(savedRecord);
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

            // Update AppointmentRecord fields
            appointmentRecordMapper.update(appointmentRecord, request);

            // Update Prescription
            prescriptionService.updatePrescription(appointmentRecord, request);

            // Update relationships
            updateRelationships(appointmentRecord, request);

            appointmentRecordRepository.save(appointmentRecord);
            loggingService.saveLog(LoggingDTO.builder().message("Appointment record with id " + appointmentRecord.getId() + " updated").type(LoggingType.APPOINTMENT_UPDATED).build());
            return appointmentRecordMapper.toDTO(appointmentRecord);
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    private void updateRelationships(@NonNull AppointmentRecord appointmentRecord, @NonNull UpdateAppointmentRequestDTO request) {
        if(request.getDoctorId() != null) {
            appointmentRecord.setDoctor(doctorRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + request.getDoctorId())));
        }

        if(request.getUserId() != null) {
            appointmentRecord.setUser(userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.getUserId())));
        }

        if(request.getHealthProviderId() != null) {
            appointmentRecord.setHealthProvider(healthProviderRepository.findById(request.getHealthProviderId())
                    .orElseThrow(() -> new EntityNotFoundException("Health Provider not found with ID: " + request.getHealthProviderId())));
        }
    }

    public String deleteAppointmentRecord(Long appointmentRecordId) {
        try {
            AppointmentRecord appointmentRecord = appointmentRecordRepository.findByIdActive(appointmentRecordId);
            if(appointmentRecord == null) {
                throw new ConflictException("Appointment Record not found with ID: " + appointmentRecordId);
            }
            Long doctorId = appointmentRecord.getDoctor().getId();
            LocalDateTime scheduledAt = appointmentRecord.getScheduledAt();
            DoctorScheduleDTO doctorScheduleDTO = DoctorScheduleDTO.builder()
                    .doctorId(doctorId)
                    .startTime(scheduledAt)
                    .endTime(scheduledAt.plusMinutes(60))
                    .currentPatientCount(-1)
                    .build();
            //update doctor schedule
            doctorScheduleService.updateOrCreateDoctorSchedule(doctorScheduleDTO, false);
            appointmentRecord.setStatus(AppointmentStatus.CANCELLED);
            appointmentRecordRepository.deleteById(appointmentRecordId);
            loggingService.saveLog(LoggingDTO.builder().message("Appointment record with id " + appointmentRecordId + " deleted").type(LoggingType.APPOINTMENT_DELETED).build());
            return "Appointment Record deleted successfully";
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ConflictException(e.getMessage());
        }
    }

    public List<AppointmentRecordDTO> findAll(){
        try{
            List<AppointmentRecord> appointmentRecords = appointmentRecordRepository.findAll();
            return appointmentRecords.stream().map(appointmentRecordMapper::toDTO).toList();
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public AppointmentRecordDTO getById(Long id){
        try{
            AppointmentRecord appointmentRecord = appointmentRecordRepository.findById(id).orElse(null);
            if(appointmentRecord == null) {
                throw new ConflictException("Appointment Record not found with ID: " + id);
            }
            return appointmentRecordMapper.toDTO(appointmentRecord);
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public List<AppointmentRecordDTO> getByUserId(Long userId){
        try{
            List<AppointmentRecord> appointmentRecords = appointmentRecordRepository.findByUserId(userId);
            return appointmentRecords.stream().map(appointmentRecordMapper::toDTO).toList();
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public List<AppointmentRecordDTO> getByDoctorId(Long doctorId){
        try{
            List<AppointmentRecord> appointmentRecords = appointmentRecordRepository.findByDoctorId(doctorId);
            return appointmentRecords.stream().map(appointmentRecordMapper::toDTO).toList();
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }
}
