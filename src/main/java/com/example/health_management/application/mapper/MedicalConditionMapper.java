package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.medical_condition.MedicalConditionDTO;
import com.example.health_management.domain.entities.MedicalConditions;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicalConditionMapper {
    MedicalConditionDTO toDTO(MedicalConditions medicalConditions);

    MedicalConditions toEntity(MedicalConditionDTO medicalConditionDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    MedicalConditions update(MedicalConditionDTO medicalConditionDTO, @MappingTarget MedicalConditions medicalConditions);
}
