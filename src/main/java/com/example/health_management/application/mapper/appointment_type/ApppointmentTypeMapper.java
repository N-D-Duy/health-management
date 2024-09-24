package com.example.health_management.application.mapper.appointment_type;

import com.example.health_management.application.DTOs.appointment_type.ApppointmentTypeResponseDto;
import com.example.health_management.domain.entities.AppointmentType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApppointmentTypeMapper {
    ApppointmentTypeResponseDto toDto(AppointmentType appointmentType);
    AppointmentType toEntity(ApppointmentTypeResponseDto apppointmentTypeDto);
}
