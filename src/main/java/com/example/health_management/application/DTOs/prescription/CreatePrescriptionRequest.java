package com.example.health_management.application.DTOs.prescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.example.health_management.domain.entities.Prescription}
 */
@Value
@Builder
@AllArgsConstructor
public class CreatePrescriptionRequest implements Serializable {
    Long userId;
    String diagnosis;
    Long doctorId;
    String notes;
    Set<Long> details;
}