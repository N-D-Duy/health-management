package com.example.health_management.application.mapper.appointment;

import com.example.health_management.application.DTOs.appointment.CreateAppointmentRequestDto;
import com.example.health_management.application.DTOs.appointment.AppointmentResponseDto;
import com.example.health_management.domain.entities.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentResponseDto toAppointmentResponseDto(Appointment appointment);

    @Mapping(target = "healthProvider", source = "healthProvider")
    @Mapping(target = "appointmentType", source = "appointmentType")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", ignore = true)
    Appointment toEntity(CreateAppointmentRequestDto createAppointmentRequestDto, HealthProvider healthProvider, AppointmentType appointmentType, User user);

    @Mapping(target = "healthProvider", source = "healthProvider")
    @Mapping(target = "appointmentType", source = "appointmentType")
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Appointment partialUpdateFromAppointmentRequestDto(CreateAppointmentRequestDto createAppointmentRequestDto, HealthProvider healthProvider, AppointmentType appointmentType, @MappingTarget Appointment appointment);

}
