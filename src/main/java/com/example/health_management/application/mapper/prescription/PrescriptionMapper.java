package com.example.health_management.application.mapper.prescription;

import com.example.health_management.application.DTOs.prescription.CreatePrescriptionDto;
import com.example.health_management.application.DTOs.prescription.PrescriptionResponseDto;
import com.example.health_management.domain.entities.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import javax.print.Doc;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
    @Mapping(target = "user", source = "user")
    @Mapping(target = "doctor", source = "doctor")
    @Mapping(target = "details", source = "details")
    @Mapping(target = "id", ignore = true)
     Prescription toPrescription(CreatePrescriptionDto createPrescriptionDto, User user, Doctor doctor, List<PrescriptionDetails> details);

    @Mapping(target = "user", source = "user")
     PrescriptionResponseDto toPrescriptionResponseDto(Prescription prescription);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "doctor", source = "doctor")
    @Mapping(target = "details", source = "details")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
     Prescription partialUpdate(CreatePrescriptionDto createPrescriptionDto, @MappingTarget Prescription prescription, User user, Doctor doctor, List<PrescriptionDetails> details);
}
