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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicalConditionService {
    private final MedicalConditionRepository medicalConditionRepository;
    private final MedicalConditionMapper medicalConditionMapper;

    public Set<MedicalConditions> updateMedicalConditions(Prescription prescription,
                                                          Set<MedicalConditionDTO> conditionDTOs) {
        Set<MedicalConditions> currentConditions = prescription.getMedicalConditions();

        Set<MedicalConditionDTO> existingConditionDTOs = conditionDTOs.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.toSet());

        Set<MedicalConditionDTO> newConditionDTOs = conditionDTOs.stream()
                .filter(dto -> dto.getId() == null)
                .collect(Collectors.toSet());

        for (MedicalConditionDTO conditionDTO : existingConditionDTOs) {
            MedicalConditions condition = currentConditions.stream()
                    .filter(c -> c.getId().equals(conditionDTO.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ConflictException("MedicalCondition not found"));

            medicalConditionMapper.update(conditionDTO, condition);
        }

        for (MedicalConditionDTO conditionDTO : newConditionDTOs) {
            MedicalConditions newCondition = new MedicalConditions();
            medicalConditionMapper.update(conditionDTO, newCondition);
            newCondition.setPrescription(prescription);
            currentConditions.add(newCondition);
        }

        return currentConditions;
    }
}
