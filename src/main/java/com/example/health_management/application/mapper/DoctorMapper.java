package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.user.response.DoctorDTO;
import com.example.health_management.application.DTOs.user.response.DoctorSummaryDTO;
import com.example.health_management.domain.entities.Doctor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDTO toDoctorDTO(Doctor doctor);

    Doctor toEntity(DoctorDTO doctorDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "healthProvider", ignore = true)
    @Mapping(target = "rating", source = "doctorDTO.rating")
    @Mapping(target = "qualification", source = "doctorDTO.qualification")
    @Mapping(target = "experience", source = "doctorDTO.experience")
    @Mapping(target = "specialization", source = "doctorDTO.specialization")
    @Mapping(target = "about", source = "doctorDTO.about")
    void updateDoctor(DoctorDTO doctorDTO, @MappingTarget Doctor doctor);

    @Mapping(target = "id", source = "doctor.id")
    @Mapping(target = "firstName", source = "doctor.user.firstName")
    @Mapping(target = "lastName", source = "doctor.user.lastName")
    @Mapping(target = "avatarUrl", source = "doctor.user.avatarUrl")
    @Mapping(target = "email", source = "doctor.user.account.email")
    @Mapping(target = "role", source = "doctor.user.account.role")
    @Mapping(target = "specialization", source = "doctor.specialization")
    @Mapping(target = "experience", source = "doctor.experience")
    @Mapping(target = "qualification", source = "doctor.qualification")
    @Mapping(target = "rating", source = "doctor.rating")
    @Mapping(target = "about", source = "doctor.about")
    DoctorSummaryDTO toSummary(Doctor doctor);
}
