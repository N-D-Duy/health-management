package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.prescription_details.PrescriptionDetailsDTO;
import com.example.health_management.application.mapper.PrescriptionDetailsMapper;
import com.example.health_management.domain.entities.Prescription;
import com.example.health_management.domain.entities.PrescriptionDetails;
import com.example.health_management.domain.repositories.PrescriptionDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class PrescriptionDetailsService {
    private final PrescriptionDetailsRepository prescriptionDetailsRepository;
    private final PrescriptionDetailsMapper prescriptionDetailsMapper;

    public Set<PrescriptionDetails> updatePrescriptionDetails(Prescription prescription,
                                                              Set<PrescriptionDetailsDTO> detailsDTOs) {
        Set<PrescriptionDetails> detailsSet = new HashSet<>();

        for (PrescriptionDetailsDTO detailsDTO : detailsDTOs) {
            if (detailsDTO.getId() != null) {
                // Update existing details
                PrescriptionDetails details = prescriptionDetailsRepository
                        .findById(detailsDTO.getId())
                        .orElseThrow(() -> new RuntimeException("PrescriptionDetails not found"));
                prescriptionDetailsMapper.update(details, detailsDTO);
                detailsSet.add(details);
            } else {
                // Create new details
                PrescriptionDetails newDetails = prescriptionDetailsMapper.toEntity(detailsDTO);
                newDetails.setPrescription(prescription);
                detailsSet.add(newDetails);
            }
        }

        return detailsSet;
    }
}
