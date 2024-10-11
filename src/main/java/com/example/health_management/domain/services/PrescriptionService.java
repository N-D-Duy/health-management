//package com.example.health_management.domain.services;
//
//import com.example.health_management.application.DTOs.medical_condition.MedicalConditionDTO;
//import com.example.health_management.application.DTOs.prescription.PrescriptionDTO;
//import com.example.health_management.application.DTOs.prescription_details.PrescriptionDetailsDTO;
//import com.example.health_management.application.guards.JwtProvider;
//import com.example.health_management.application.mapper.MedicalConditionMapper;
//import com.example.health_management.application.mapper.PrescriptionDetailsMapper;
//import com.example.health_management.application.mapper.PrescriptionMapper;
//import com.example.health_management.domain.entities.*;
//import com.example.health_management.domain.repositories.*;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class PrescriptionService {
//
//    private final PrescriptionRepository prescriptionRepository;
//    private final MedicationRepository medicationRepository;
//    private final UserRepository userRepository;
//    private final PrescriptionMapper prescriptionMapper;
//    private final PrescriptionDetailsMapper prescriptionDetailsMapper;
//    private final JwtProvider jwtService;
//    private final PrescriptionDetailsRepository prescriptionDetailsRepository;
//    private final DoctorRepository doctorRepository;
//    private final MedicalConditionMapper medicalConditionMapper;
//    private final MedicalConditionRepository medicalConditionRepository;
//
//    @Transactional
//    public PrescriptionDTO create(PrescriptionRequest request) {
//        try {
//            // Create medical conditions
//            Set<MedicalConditions> medicalConditions = request.getMedicalConditions().stream()
//                    .map(medicalConditionMapper::toEntity)
//                    .collect(Collectors.toSet());
//
//            // Create prescription details and associate medications
//            Set<PrescriptionDetails> details = request.getDetails().stream()
//                    .map(prescriptionDetailsRequest -> {
//                        PrescriptionDetails prescriptionDetails = prescriptionDetailsMapper.toEntity(prescriptionDetailsRequest);
//                        Medication medication = medicationRepository.findById(prescriptionDetailsRequest.getMedicationId())
//                                .orElseThrow(() -> new RuntimeException("Medication not found with ID: " + prescriptionDetailsRequest.getMedicationId()));
//                        prescriptionDetails.setMedication(medication);
//                        return prescriptionDetails;
//                    })
//                    .collect(Collectors.toSet());
//
//            // Create the prescription entity
//            Prescription prescription = new Prescription();
//            prescription.setDiagnosis(request.getDiagnosis());
//            prescription.setNotes(request.getNotes());
//            prescription.setMedicalConditions(medicalConditions);
//            prescription.setDetails(details);
//
//            details.forEach(detail -> detail.setPrescription(prescription));
//            medicalConditions.forEach(condition -> condition.setPrescription(prescription));
//            // Save the prescription
//            Prescription savedPrescription = prescriptionRepository.save(prescription);
//
//
//            // Return DTO after saving
//            return prescriptionMapper.toDTO(savedPrescription);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error creating prescription: " + e.getMessage(), e);
//        }
//    }
//
//
//    @Transactional
//    public PrescriptionDTO update(PrescriptionDTO request) {
//        try {
//            Prescription prescription = prescriptionRepository.findById(request.getId())
//                    .orElseThrow(() -> new RuntimeException("Prescription not found with ID: " + request.getId()));
//
//            // Update Prescription core fields
//            prescription.setDiagnosis(request.getDiagnosis());
//            prescription.setNotes(request.getNotes());
//
//            // Update Prescription Details
//            if (request.getDetails() != null && !request.getDetails().isEmpty()) {
//                Set<PrescriptionDetails> detailsSet = new HashSet<>();
//                for (PrescriptionDetailsDTO detailsRequest : request.getDetails()) {
//                    PrescriptionDetails details;
//
//                    if (detailsRequest.getId() != null) {
//                        details = prescriptionDetailsRepository.findById(detailsRequest.getId())
//                                .orElseThrow(() -> new RuntimeException("Prescription Details not found with ID: " + detailsRequest.getId()));
//                        prescriptionDetailsMapper.update(details, detailsRequest);  // Map updated fields
//                    } else {
//                        details = prescriptionDetailsMapper.toEntity(detailsRequest);  // New details
//                    }
//
//                    Medication medication = medicationRepository.findById(detailsRequest.getMedicationId())
//                            .orElseThrow(() -> new RuntimeException("Medication not found with ID: " + detailsRequest.getMedicationId()));
//                    details.setMedication(medication);
//                    details.setPrescription(prescription);  // Ensure the relationship is set
//                    detailsSet.add(details);
//                }
//
//                // Update the PrescriptionDetails set
//                prescription.setDetails(detailsSet);
//            }
//
//            // Update Medical Conditions
//            if (request.getMedicalConditions() != null && !request.getMedicalConditions().isEmpty()) {
//                Set<MedicalConditions> medicalConditionSet = new HashSet<>();
//                for (MedicalConditionDTO medicalConditionDTO : request.getMedicalConditions()) {
//                    MedicalConditions medicalCondition;
//
//                    if (medicalConditionDTO.getId() != null) {
//                        medicalCondition = medicalConditionRepository.findById(medicalConditionDTO.getId())
//                                .orElseThrow(() -> new RuntimeException("MedicalCondition not found with ID: " + medicalConditionDTO.getId()));
//                        medicalConditionMapper.update(medicalConditionDTO, medicalCondition);  // Update existing
//                    } else {
//                        medicalCondition = medicalConditionMapper.toEntity(medicalConditionDTO);  // New condition
//                    }
//
//                    medicalCondition.setPrescription(prescription);  // Ensure the relationship is set
//                    medicalConditionSet.add(medicalCondition);
//                }
//
//                // Update the MedicalConditions set
//                prescription.setMedicalConditions(medicalConditionSet);
//            }
//
//            // Save the updated Prescription
//            Prescription updatedPrescription = prescriptionRepository.save(prescription);
//            return prescriptionMapper.toDTO(updatedPrescription);
//
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error updating prescription: " + e.getMessage(), e);
//        }
//    }
//
//    public List<PrescriptionDTO> getAllPrescriptions() {
//        try {
//            return prescriptionRepository.findAll().stream()
//                    .map(prescriptionMapper::toDTO)
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            throw new RuntimeException("Error fetching prescriptions: " + e.getMessage(), e);
//        }
//    }
//
//}
