package com.example.health_management.application.DTOs.appointment_type;

import com.example.health_management.domain.entities.AppointmentType;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link AppointmentType}
 */
@Value
public class ApppointmentTypeResponseDto implements Serializable {
    int id;
    String typeName;
    String typeDescription;
}