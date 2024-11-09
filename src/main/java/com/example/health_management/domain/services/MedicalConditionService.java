package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.medical_condition.MedicalConditionDTO;
import com.example.health_management.application.mapper.MedicalConditionMapper;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.entities.MedicalConditions;
import com.example.health_management.domain.entities.Prescription;
import com.example.health_management.domain.repositories.MedicalConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicalConditionService {
    private final MedicalConditionRepository medicalConditionRepository;
    private final MedicalConditionMapper medicalConditionMapper;

    public Set<MedicalConditions> updateMedicalConditions(Prescription prescription,
                                                          Set<MedicalConditionDTO> conditionDTOs) {
        Set<MedicalConditions> medicalConditionSet = new HashSet<>();

        for (MedicalConditionDTO conditionDTO : conditionDTOs) {
            if (conditionDTO.getId() != null) {
                // Update existing condition
                MedicalConditions condition = medicalConditionRepository
                        .findById(conditionDTO.getId())
                        .orElseThrow(() -> new ConflictException("MedicalCondition not found"));
                medicalConditionMapper.update(conditionDTO, condition);
                medicalConditionSet.add(condition);
            } else {
                // Create new condition
                MedicalConditions newCondition = medicalConditionMapper.toEntity(conditionDTO);
                newCondition.setPrescription(prescription);
                medicalConditionSet.add(newCondition);
            }
        }

        return medicalConditionSet;
    }
}
