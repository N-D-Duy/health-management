package com.example.health_management.application.DTOs.prescription;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link com.example.health_management.domain.entities.Prescription}
 */
@Value
@Builder
@AllArgsConstructor
public class CreatePrescriptionDto implements Serializable {
    Long userId;
    String diagnosis;
    String treatment;
    LocalDate createDate;
    Set<Long> medicationIds;
}