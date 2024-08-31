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
@JsonNaming(value = com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreatePrescriptionDto implements Serializable {
    Integer userId;
    String diagnosis;
    String treatment;
    LocalDate createDate;
    Set<Integer> medicationIds;
}