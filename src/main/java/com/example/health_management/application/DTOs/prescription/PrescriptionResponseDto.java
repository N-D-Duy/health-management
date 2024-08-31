package com.example.health_management.application.DTOs.prescription;

import com.example.health_management.application.DTOs.user.UserResponseDto;
import com.example.health_management.domain.entities.Medication;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link com.example.health_management.domain.entities.Prescription}
 */
@Value
@Builder
@AllArgsConstructor
public class PrescriptionResponseDto implements Serializable {
    int id;
    String diagnosis;
    String treatment;
    LocalDate create_date;
    UserResponseDto user;
    Set<Medication> medications;
}