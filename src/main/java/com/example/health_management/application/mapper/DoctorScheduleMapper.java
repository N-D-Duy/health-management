package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.doctor.DoctorScheduleDTO;
import com.example.health_management.domain.entities.DoctorSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorScheduleMapper {
    @Mapping(target = "doctorId", source = "doctorSchedule.doctor.id")
    @Mapping(target="appointmentRecord", ignore = true)
    DoctorScheduleDTO toDTO(DoctorSchedule doctorSchedule);
    @Mapping(target = "id", ignore = true)
    DoctorSchedule toEntity(DoctorScheduleDTO doctorScheduleDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    DoctorSchedule update(@MappingTarget DoctorSchedule doctorSchedule, DoctorScheduleDTO doctorScheduleDTO);
}
