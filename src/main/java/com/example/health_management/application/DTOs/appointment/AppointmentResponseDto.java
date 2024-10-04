package com.example.health_management.application.DTOs.appointment;

import com.example.health_management.application.DTOs.heath_provider.HealthProviderResponseDto;
import com.example.health_management.application.DTOs.user.UserResponseDto;
import com.example.health_management.common.shared.enums.AppointmentType;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.example.health_management.domain.entities.Appointment}
 */
@Value
@Builder
public class AppointmentResponseDto implements Serializable {
    long id;
    LocalDate date;
    HealthProviderResponseDto healthProvider;
    UserResponseDto user;
    AppointmentType appointmentType;
}