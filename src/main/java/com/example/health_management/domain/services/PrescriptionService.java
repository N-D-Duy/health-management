package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.prescription.CreatePrescriptionRequest;
import com.example.health_management.application.DTOs.prescription.PrescriptionDTO;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.mapper.PrescriptionMapper;
import com.example.health_management.common.shared.enums.Role;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.Prescription;
import com.example.health_management.domain.entities.PrescriptionDetails;
import com.example.health_management.domain.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final JwtProvider jwtService;
    private final PrescriptionDetailsRepository prescriptionDetailsRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository, MedicationRepository medicationRepository, UserRepository userRepository, PrescriptionMapper prescriptionMapper, JwtProvider jwtService, PrescriptionDetailsRepository prescriptionDetailsRepository, DoctorRepository doctorRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.medicationRepository = medicationRepository;
        this.userRepository = userRepository;
        this.prescriptionMapper = prescriptionMapper;
        this.jwtService = jwtService;
        this.prescriptionDetailsRepository = prescriptionDetailsRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<PrescriptionDTO> getAllPrescriptions(@Nullable Long userId) {
        try {
            Role role = jwtService.extractUserFromToken().getAccount().getRole();
            userId = ((userId != null && role.equals(Role.DOCTOR)) ? userId : jwtService.extractUserFromToken().getId());
            List<Prescription> prescriptions = prescriptionRepository.findByUser_Id(userId);
            return prescriptions.stream().map(prescriptionMapper::toPrescriptionResponseDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public PrescriptionDTO getPrescriptionById(Long id, @Nullable Long userId) {
        try {
            Role role = jwtService.extractUserFromToken().getAccount().getRole();
            userId = ((userId != null && role.equals(Role.DOCTOR)) ? userId : jwtService.extractUserFromToken().getId());
            return prescriptionMapper.toPrescriptionResponseDto(prescriptionRepository.findByUser_IdAndId(userId, id)
                    .orElseThrow(() -> new RuntimeException("Prescription not found")));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public PrescriptionDTO createPrescription(CreatePrescriptionRequest createPrescriptionRequest) {
        try {
            List<PrescriptionDetails> details = createPrescriptionRequest.getDetails().stream().map(prescriptionDetail -> prescriptionDetailsRepository.findById(prescriptionDetail).orElseThrow(() -> new RuntimeException("Medication not found !!!"))).collect(Collectors.toList());
            Doctor doctor = doctorRepository.findById(createPrescriptionRequest.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor not found"));
            Prescription prescription = prescriptionMapper.toPrescription(createPrescriptionRequest, userRepository.findById(createPrescriptionRequest.getUserId()).get(), doctor, details);
            return prescriptionMapper.toPrescriptionResponseDto(prescriptionRepository.save(prescription));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String deletePrescription(Long userId,Long id) {
        try {
            Prescription prescription = prescriptionRepository.findByUser_IdAndId(userId, id).orElseThrow(() -> new RuntimeException("Prescription not found"));
            prescriptionRepository.delete(prescription);
            return "Prescription deleted successfully";
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public PrescriptionDTO updatePrescription(Long id, CreatePrescriptionRequest createPrescriptionRequest) {
        try {
            Prescription prescription = prescriptionRepository.findByUser_IdAndId(createPrescriptionRequest.getUserId(), id).orElseThrow(() -> new RuntimeException("Prescription not found"));
            List<PrescriptionDetails> details = createPrescriptionRequest.getDetails().stream().map(prescriptionDetail -> prescriptionDetailsRepository.findById(prescriptionDetail).orElseThrow(() -> new RuntimeException("Medication not found !!!"))).collect(Collectors.toList());
            Doctor doctor = doctorRepository.findById(createPrescriptionRequest.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor not found"));
            prescription = prescriptionMapper.partialUpdate(createPrescriptionRequest, prescription, userRepository.findById(createPrescriptionRequest.getUserId()).get(), doctor, details);
            return prescriptionMapper.toPrescriptionResponseDto(prescriptionRepository.save(prescription));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
