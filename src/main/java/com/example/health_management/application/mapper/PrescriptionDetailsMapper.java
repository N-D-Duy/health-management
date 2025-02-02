package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.prescription_details.PrescriptionDetailsDTO;
import com.example.health_management.domain.entities.PrescriptionDetails;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PrescriptionDetailsMapper {
    PrescriptionDetailsDTO toDTO(PrescriptionDetails prescriptionDetails);

    PrescriptionDetails toEntity(PrescriptionDetailsDTO prescriptionDetailsDTO);


    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    PrescriptionDetails update(@MappingTarget PrescriptionDetails prescriptionDetails, PrescriptionDetailsDTO prescriptionDetailsDTO);
}
