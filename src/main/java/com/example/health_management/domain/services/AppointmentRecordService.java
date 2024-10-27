package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.appointment_record.request.AppointmentRecordRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.request.UpdateAppointmentRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.response.AppointmentRecordDTO;
import com.example.health_management.application.mapper.AppointmentRecordMapper;
import com.example.health_management.domain.entities.AppointmentRecord;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.HealthProvider;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AppointmentRecordRepository;
import com.example.health_management.domain.repositories.DoctorRepository;
import com.example.health_management.domain.repositories.HealthProviderRepository;
import com.example.health_management.domain.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            return appointmentRecordMapper.toDTO(savedRecord);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating appointment record: " + e.getMessage(), e);
        }
    }

    public AppointmentRecordDTO update(UpdateAppointmentRequestDTO request) {
        try {
            AppointmentRecord appointmentRecord = appointmentRecordRepository
                    .findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("AppointmentRecord not found"));

            // Update AppointmentRecord fields
            appointmentRecordMapper.update(appointmentRecord, request);

            // Update Prescription
            prescriptionService.updatePrescription(appointmentRecord, request);

            // Update relationships
            updateRelationships(appointmentRecord, request);

            appointmentRecordRepository.save(appointmentRecord);
            return appointmentRecordMapper.toDTO(appointmentRecord);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            appointmentRecordRepository.deleteById(appointmentRecordId); //soft delete
            return "Appointment Record deleted successfully";
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<AppointmentRecordDTO> findAll(){
        try{
            List<AppointmentRecord> appointmentRecords = appointmentRecordRepository.findAllActive();
            return appointmentRecords.stream().map(appointmentRecordMapper::toDTO).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
