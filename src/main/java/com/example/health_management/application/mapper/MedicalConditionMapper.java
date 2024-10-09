package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.medical_condition.MedicalConditionDTO;
import com.example.health_management.application.DTOs.medication.MedicalConditionRequest;
import com.example.health_management.domain.entities.MedicalConditions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalConditionMapper {
    MedicalConditionDTO toDTO(MedicalConditions medicalConditions);

    MedicalConditions toEntity(MedicalConditionDTO medicalConditionDTO);

    @Mapping(target = "id", ignore = true)
    MedicalConditions toEntity(MedicalConditionRequest medicalConditionRequest);
}
