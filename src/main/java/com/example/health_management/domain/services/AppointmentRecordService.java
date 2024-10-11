package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.appointment_record.request.AppointmentRecordRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.request.UpdateAppointmentRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.response.AppointmentRecordDTO;
import com.example.health_management.application.DTOs.medical_condition.MedicalConditionDTO;
import com.example.health_management.application.DTOs.prescription_details.PrescriptionDetailsDTO;
import com.example.health_management.application.mapper.AppointmentRecordMapper;
import com.example.health_management.application.mapper.MedicalConditionMapper;
import com.example.health_management.application.mapper.PrescriptionDetailsMapper;
import com.example.health_management.domain.entities.*;
import com.example.health_management.domain.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentRecordService {
    private final AppointmentRecordRepository appointmentRecordRepository;
    private final PrescriptionDetailsRepository prescriptionDetailsRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRecordMapper appointmentRecordMapper;
    private final PrescriptionDetailsMapper prescriptionDetailsMapper;
    private final HealthProviderRepository healthProviderRepository;
    private final MedicalConditionRepository medicalConditionRepository;
    private final MedicalConditionMapper medicalConditionMapper;


    @Transactional
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

    @Transactional
    public AppointmentRecordDTO update(UpdateAppointmentRequestDTO request) {
        try{
            AppointmentRecord appointmentRecord = appointmentRecordRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("AppointmentRecord not found"));


            // Update Prescription if present
            if (request.getPrescription() != null) {
                Prescription prescription = appointmentRecord.getPrescription();
                if (prescription == null) {
                    prescription = new Prescription();
                    prescription.setAppointmentRecord(appointmentRecord);
                }

                // Update PrescriptionDetails
                Set<PrescriptionDetails> detailsSet = prescription.getDetails();
                for (PrescriptionDetailsDTO detailsDTO : request.getPrescription().getDetails()) {
                    if (detailsDTO.getId() != null) {
                        // Update existing details
                        PrescriptionDetails details = prescriptionDetailsRepository.findById(detailsDTO.getId())
                                .orElseThrow(() -> new RuntimeException("PrescriptionDetails not found"));
                        prescriptionDetailsMapper.update(details, detailsDTO);
                    } else {
                        // Create new details
                        PrescriptionDetails newDetails = prescriptionDetailsMapper.toEntity(detailsDTO);
                        newDetails.setPrescription(prescription);
                        detailsSet.add(newDetails);
                    }
                }

                // Update MedicalConditions
                Set<MedicalConditions> medicalConditionSet = prescription.getMedicalConditions();
                for (MedicalConditionDTO conditionDTO : request.getPrescription().getMedicalConditions()) {
                    if (conditionDTO.getId() != null) {
                        MedicalConditions condition = medicalConditionRepository.findById(conditionDTO.getId())
                                .orElseThrow(() -> new RuntimeException("MedicalCondition not found"));
                        medicalConditionMapper.update(conditionDTO, condition);
                    } else {
                        MedicalConditions newCondition = medicalConditionMapper.toEntity(conditionDTO);
                        newCondition.setPrescription(prescription);
                        medicalConditionSet.add(newCondition);
                    }
                }

                prescription.setDetails(detailsSet);
                prescription.setMedicalConditions(medicalConditionSet);
                appointmentRecord.setPrescription(prescription);
            }

            appointmentRecordMapper.update(appointmentRecord, request);

            if(request.getDoctorId()!=null) {
                appointmentRecord.setDoctor(doctorRepository.findById(request.getDoctorId())
                        .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + request.getDoctorId())));
            }

            if(request.getUserId()!=null) {
                appointmentRecord.setUser(userRepository.findById(request.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.getUserId())));
            }

            if(request.getHealthProviderId()!=null) {
                appointmentRecord.setHealthProvider(healthProviderRepository.findById(request.getHealthProviderId())
                        .orElseThrow(() -> new EntityNotFoundException("Health Provider not found with ID: " + request.getHealthProviderId())));
            }

            appointmentRecordRepository.save(appointmentRecord);
            return appointmentRecordMapper.toDTO(appointmentRecord);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
