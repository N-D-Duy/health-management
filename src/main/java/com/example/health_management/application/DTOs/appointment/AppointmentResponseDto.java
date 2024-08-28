package com.example.health_management.application.DTOs.appointment;

import com.example.health_management.application.DTOs.appointment_type.ApppointmentTypeResponseDto;
import com.example.health_management.application.DTOs.heath_provider.HealthProviderResponseDto;
import com.example.health_management.application.DTOs.user.UserResponseDto;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.example.health_management.domain.entities.Appointment}
 */
@Value
public class AppointmentResponseDto implements Serializable {
    long id;
    LocalDate date;
    HealthProviderResponseDto healthProvider;
    UserResponseDto user;
    ApppointmentTypeResponseDto appointmentType;
}