package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.prescription.PrescriptionDTO;
import com.example.health_management.domain.entities.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class, DoctorMapper.class, PrescriptionDetailsMapper.class, MedicalConditionMapper.class})
public interface PrescriptionMapper {
    @Mapping(target = "id", ignore = true)

    Prescription toEntity(PrescriptionDTO prescriptionDTO);

    PrescriptionDTO toDTO(Prescription prescription);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Prescription update(PrescriptionDTO prescriptionRequest, @MappingTarget Prescription prescription);
}
