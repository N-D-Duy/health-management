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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PrescriptionDetailsService {
    private final PrescriptionDetailsMapper prescriptionDetailsMapper;

    public Set<PrescriptionDetails> updatePrescriptionDetails(Prescription prescription,
                                                              Set<PrescriptionDetailsDTO> detailsDTOs) {
        Set<PrescriptionDetails> currentDetails = prescription.getDetails();

        Set<Long> incomingIds = detailsDTOs.stream()
                .map(PrescriptionDetailsDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        currentDetails.removeIf(details -> !incomingIds.contains(details.getId()));

        for (PrescriptionDetailsDTO detailsDTO : detailsDTOs) {
            if (detailsDTO.getId() != null) {
                PrescriptionDetails details = currentDetails.stream()
                        .filter(d -> Objects.equals(d.getId(), detailsDTO.getId()))
                        .findFirst()
                        .orElseThrow(() -> new ConflictException("PrescriptionDetails not found"));

                prescriptionDetailsMapper.update(details, detailsDTO);
            } else {
                PrescriptionDetails newDetails = prescriptionDetailsMapper.toEntity(detailsDTO);
                newDetails.setPrescription(prescription);
                currentDetails.add(newDetails);
            }
        }

        return currentDetails;
    }

}
