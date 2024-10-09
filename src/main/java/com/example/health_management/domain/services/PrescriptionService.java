package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.prescription.PrescriptionDTO;
import com.example.health_management.application.DTOs.prescription.PrescriptionRequest;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.mapper.MedicalConditionMapper;
import com.example.health_management.application.mapper.PrescriptionDetailsMapper;
import com.example.health_management.application.mapper.PrescriptionMapper;
import com.example.health_management.common.shared.enums.Role;
import com.example.health_management.domain.entities.*;
import com.example.health_management.domain.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final PrescriptionDetailsMapper prescriptionDetailsMapper;
    private final JwtProvider jwtService;
    private final PrescriptionDetailsRepository prescriptionDetailsRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalConditionMapper medicalConditionMapper;
    private final MedicalConditionRepository medicalConditionRepository;

    public PrescriptionDTO create(PrescriptionRequest request) {
        try {
            // Create medical conditions
            Set<MedicalConditions> medicalConditions = request.getMedicalConditions().stream()
                    .map(medicalConditionMapper::toEntity)
                    .collect(Collectors.toSet());

            // Create prescription details and associate medications
            Set<PrescriptionDetails> details = request.getDetails().stream()
                    .map(prescriptionDetailsRequest -> {
                        PrescriptionDetails prescriptionDetails = prescriptionDetailsMapper.toEntity(prescriptionDetailsRequest);
                        Medication medication = medicationRepository.findById(prescriptionDetailsRequest.getMedicationId())
                                .orElseThrow(() -> new RuntimeException("Medication not found with ID: " + prescriptionDetailsRequest.getMedicationId()));
                        prescriptionDetails.setMedication(medication);
                        return prescriptionDetails;
                    })
                    .collect(Collectors.toSet());

            // Create the prescription entity
            Prescription prescription = new Prescription();
            prescription.setDiagnosis(request.getDiagnosis());
            prescription.setNotes(request.getNotes());
            prescription.setMedicalConditions(medicalConditions);
            prescription.setDetails(details);

            details.forEach(detail -> detail.setPrescription(prescription));
            medicalConditions.forEach(condition -> condition.setPrescription(prescription));
            // Save the prescription
            Prescription savedPrescription = prescriptionRepository.save(prescription);


            // Return DTO after saving
            return prescriptionMapper.toDTO(savedPrescription);

        } catch (Exception e) {
            throw new RuntimeException("Error creating prescription: " + e.getMessage(), e);
        }
    }


    public List<PrescriptionDTO> getAllPrescriptions() {
        try {
            return prescriptionRepository.findAll().stream()
                    .map(prescriptionMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching prescriptions: " + e.getMessage(), e);
        }
    }

}
