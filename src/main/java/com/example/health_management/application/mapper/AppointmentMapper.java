package com.example.health_management.application.mapper;

import com.example.health_management.application.DTOs.appointment.request.CreateAppointmentRequest;
import com.example.health_management.application.DTOs.appointment.response.AppointmentResponse;
import com.example.health_management.domain.entities.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentResponse toAppointmentResponseDto(Appointment appointment);

    @Mapping(target = "id", ignore = true)
    Appointment toEntity(CreateAppointmentRequest createAppointmentRequest, HealthProvider healthProvider, User user, Doctor doctor);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Appointment partialUpdateFromAppointmentRequestDto(CreateAppointmentRequest createAppointmentRequest, HealthProvider healthProvider, User user, Doctor doctor, @MappingTarget Appointment appointment);

}
