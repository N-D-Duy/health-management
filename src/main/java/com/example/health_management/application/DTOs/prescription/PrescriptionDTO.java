package com.example.health_management.application.DTOs.prescription;

import com.example.health_management.application.DTOs.medical_condition.MedicalConditionDTO;
import com.example.health_management.application.DTOs.medication.MedicationDTO;
import com.example.health_management.application.DTOs.prescription_details.PrescriptionDetailsDTO;
import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.example.health_management.domain.entities.Medication;
import com.example.health_management.domain.entities.PrescriptionDetails;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link com.example.health_management.domain.entities.Prescription}
 */
@Builder
@Data
public class PrescriptionDTO implements Serializable {
    private Long id;
    private String diagnosis;
    private String notes;
    private Set<PrescriptionDetailsDTO> details;
    private Set<MedicalConditionDTO> medicalConditions;
}