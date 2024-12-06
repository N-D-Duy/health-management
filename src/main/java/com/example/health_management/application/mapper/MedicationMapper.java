package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.medication.MedicationDTO;
import com.example.health_management.domain.entities.Medication;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MedicationMapper {
    @Mapping(target = "id", ignore = true)
    Medication toEntity(MedicationDTO medicationDTO);
    MedicationDTO toDTO(Medication medication);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Medication updateFromDTO(MedicationDTO medicationDTO, @MappingTarget Medication medication);
}
