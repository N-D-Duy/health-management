package com.example.health_management.application.mapper.prescription;

import com.example.health_management.application.DTOs.prescription.CreatePrescriptionDto;
import com.example.health_management.application.DTOs.prescription.PrescriptionResponseDto;
import com.example.health_management.domain.entities.Medication;
import com.example.health_management.domain.entities.Prescription;
import com.example.health_management.domain.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
//    @Mapping(target = "user", source = "user")
//    @Mapping(target = "medications", source = "medications")
//    @Mapping(target = "id", ignore = true)
//     Prescription toPrescription(CreatePrescriptionDto createPrescriptionDto, User user, Set<Medication> medications);
//
//    @Mapping(target = "user", source = "user")
//     PrescriptionResponseDto toPrescriptionResponseDto(Prescription prescription);
//
//    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(target = "user", source = "user")
//    @Mapping(target = "medications", source = "medications")
//    @Mapping(target = "id", ignore = true)
//     Prescription partialUpdate(CreatePrescriptionDto createPrescriptionDto, @MappingTarget Prescription prescription, User user, Set<Medication> medications);
}
