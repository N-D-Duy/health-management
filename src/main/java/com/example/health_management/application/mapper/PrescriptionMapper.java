package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.prescription.CreatePrescriptionRequest;
import com.example.health_management.application.DTOs.prescription.PrescriptionDTO;
import com.example.health_management.domain.entities.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
    @Mapping(target = "user", source = "user")
    @Mapping(target = "doctor", source = "doctor")
    @Mapping(target = "details", source = "details")
    @Mapping(target = "id", ignore = true)
     Prescription toPrescription(CreatePrescriptionRequest createPrescriptionRequest, User user, Doctor doctor, List<PrescriptionDetails> details);

    @Mapping(target = "user", source = "user")
    PrescriptionDTO toPrescriptionResponseDto(Prescription prescription);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "doctor", source = "doctor")
    @Mapping(target = "details", source = "details")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
     Prescription partialUpdate(CreatePrescriptionRequest createPrescriptionRequest, @MappingTarget Prescription prescription, User user, Doctor doctor, List<PrescriptionDetails> details);
}
