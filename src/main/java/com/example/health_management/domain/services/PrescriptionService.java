//package com.example.health_management.domain.services;
//
//import com.example.health_management.application.DTOs.prescription.CreatePrescriptionDto;
//import com.example.health_management.application.DTOs.prescription.PrescriptionResponseDto;
//import com.example.health_management.application.guards.JwtProvider;
//import com.example.health_management.application.mapper.prescription.PrescriptionMapper;
//import com.example.health_management.common.shared.enums.Role;
//import com.example.health_management.domain.entities.Medication;
//import com.example.health_management.domain.entities.Prescription;
//import com.example.health_management.domain.repositories.MedicationRepository;
//import com.example.health_management.domain.repositories.PrescriptionRepository;
//import com.example.health_management.domain.repositories.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.lang.Nullable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//@Slf4j
//@Service
//public class PrescriptionService {
//
//    private final PrescriptionRepository prescriptionRepository;
//    private final MedicationRepository medicationRepository;
//    private final UserRepository userRepository;
//    private final PrescriptionMapper prescriptionMapper;
//    private final JwtProvider jwtService;
//
//    @Autowired
//    public PrescriptionService(PrescriptionRepository prescriptionRepository, MedicationRepository medicationRepository, UserRepository userRepository, PrescriptionMapper prescriptionMapper, JwtProvider jwtService) {
//        this.prescriptionRepository = prescriptionRepository;
//        this.medicationRepository = medicationRepository;
//        this.userRepository = userRepository;
//        this.prescriptionMapper = prescriptionMapper;
//        this.jwtService = jwtService;
//    }
//
//    public List<PrescriptionResponseDto> getAllPrescriptions(@Nullable Long userId) {
//        try {
//            Role role = jwtService.extractUserFromToken().getAccount().getRole();
//            userId = ((userId != null && role.equals(Role.DOCTOR)) ? userId : jwtService.extractUserFromToken().getId());
//            List<Prescription> prescriptions = prescriptionRepository.findByUser_Id(userId);
//            return prescriptions.stream().map(prescriptionMapper::toPrescriptionResponseDto).collect(Collectors.toList());
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public PrescriptionResponseDto getPrescriptionById(Long id, @Nullable Long userId) {
//        try {
//            Role role = jwtService.extractUserFromToken().getAccount().getRole();
//            userId = ((userId != null && role.equals(Role.DOCTOR)) ? userId : jwtService.extractUserFromToken().getId());
//            return prescriptionMapper.toPrescriptionResponseDto(prescriptionRepository.findByUser_IdAndId(userId, id)
//                    .orElseThrow(() -> new RuntimeException("Prescription not found")));
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
//
//    public PrescriptionResponseDto createPrescription(CreatePrescriptionDto createPrescriptionDto) {
//        try {
//            Set<Medication> medications = createPrescriptionDto.getMedicationIds().stream().map(medicationId -> medicationRepository.findById(medicationId).orElseThrow(() -> new RuntimeException("Medication not found !!!"))).collect(Collectors.toSet());
//            Prescription prescription = prescriptionMapper.toPrescription(createPrescriptionDto, userRepository.findById(createPrescriptionDto.getUserId()).get(), medications);
//            return prescriptionMapper.toPrescriptionResponseDto(prescriptionRepository.save(prescription));
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
//
//    public String deletePrescription(Long userId,Long id) {
//        try {
//            Prescription prescription = prescriptionRepository.findByUser_IdAndId(userId, id).orElseThrow(() -> new RuntimeException("Prescription not found"));
//            prescriptionRepository.delete(prescription);
//            return "Prescription deleted successfully";
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
//
//    public PrescriptionResponseDto updatePrescription(Long id, CreatePrescriptionDto createPrescriptionDto) {
//        try {
//            Prescription prescription = prescriptionRepository.findByUser_IdAndId(createPrescriptionDto.getUserId(), id).orElseThrow(() -> new RuntimeException("Prescription not found"));
//            Set<Medication> medications = createPrescriptionDto.getMedicationIds().stream().map(medicationId -> medicationRepository.findById(medicationId).orElseThrow(() -> new RuntimeException("Medication not found !!!"))).collect(Collectors.toSet());
//            prescription = prescriptionMapper.partialUpdate(createPrescriptionDto, prescription, userRepository.findById(createPrescriptionDto.getUserId()).get(), medications);
//            return prescriptionMapper.toPrescriptionResponseDto(prescriptionRepository.save(prescription));
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e);
//        }
//    }
//}
