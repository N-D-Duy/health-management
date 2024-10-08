package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.user.response.DoctorDTO;
import com.example.health_management.domain.entities.Doctor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDTO toDoctorDTO(Doctor doctor);

    Doctor toEntity(DoctorDTO doctorDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "rating", source = "doctorDTO.rating")
    @Mapping(target = "qualification", source = "doctorDTO.qualification")
    @Mapping(target = "experience", source = "doctorDTO.experience")
    @Mapping(target = "specialization", source = "doctorDTO.specialization")
    @Mapping(target = "about", source = "doctorDTO.about")
    Doctor updateDoctor(DoctorDTO doctorDTO, Doctor doctor);
}
