package com.example.health_management.application.mapper.appointment;

import com.example.health_management.application.DTOs.appointment.CreateAppointmentRequestDto;
import com.example.health_management.application.DTOs.appointment.AppointmentResponseDto;
import com.example.health_management.common.shared.enums.AppointmentType;
import com.example.health_management.domain.entities.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentResponseDto toAppointmentResponseDto(Appointment appointment);


    @Mapping(target = "healthProvider", source = "healthProvider")
    @Mapping(target = "appointmentType", source = "createAppointmentRequestDto.appointmentType")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "doctor", source = "doctor")
    @Mapping(target = "id", ignore = true)
    Appointment toEntity(CreateAppointmentRequestDto createAppointmentRequestDto, HealthProvider healthProvider, User user, Doctor doctor);

    @Mapping(target = "healthProvider", source = "healthProvider")
    @Mapping(target = "appointmentType", source = "createAppointmentRequestDto.appointmentType")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "doctor", source = "doctor")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Appointment partialUpdateFromAppointmentRequestDto(CreateAppointmentRequestDto createAppointmentRequestDto, HealthProvider healthProvider,User user, Doctor doctor, @MappingTarget Appointment appointment);

}
