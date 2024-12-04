package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.prescription_details.PrescriptionDetailsDTO;
import com.example.health_management.application.mapper.PrescriptionDetailsMapper;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.entities.Prescription;
import com.example.health_management.domain.entities.PrescriptionDetails;
import com.example.health_management.domain.repositories.PrescriptionDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PrescriptionDetailsService {
    private final PrescriptionDetailsRepository prescriptionDetailsRepository;
    private final PrescriptionDetailsMapper prescriptionDetailsMapper;

    public Set<PrescriptionDetails> updatePrescriptionDetails(Prescription prescription,
                                                              Set<PrescriptionDetailsDTO> detailsDTOs) {
        Set<PrescriptionDetails> currentDetails = prescription.getDetails();

        Set<PrescriptionDetailsDTO> existingDetailsDTOs = detailsDTOs.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.toSet());

        Set<PrescriptionDetailsDTO> newDetailsDTOs = detailsDTOs.stream()
                .filter(dto -> dto.getId() == null)
                .collect(Collectors.toSet());

        for (PrescriptionDetailsDTO detailsDTO : existingDetailsDTOs) {
            PrescriptionDetails details = currentDetails.stream()
                    .filter(d -> d.getId().equals(detailsDTO.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ConflictException("PrescriptionDetails not found"));

            prescriptionDetailsMapper.update(details, detailsDTO);
        }

        for (PrescriptionDetailsDTO detailsDTO : newDetailsDTOs) {
            PrescriptionDetails newDetails = prescriptionDetailsMapper.toEntity(detailsDTO);
            newDetails.setPrescription(prescription);
            currentDetails.add(newDetails);
        }

        return currentDetails;
    }
}
