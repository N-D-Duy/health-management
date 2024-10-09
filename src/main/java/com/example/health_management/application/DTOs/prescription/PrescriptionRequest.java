package com.example.health_management.application.DTOs.prescription;

import com.example.health_management.application.DTOs.medical_condition.MedicalConditionDTO;
import com.example.health_management.application.DTOs.medication.MedicalConditionRequest;
import com.example.health_management.application.DTOs.prescription_details.PrescriptionDetailsDTO;
import com.example.health_management.application.DTOs.prescription_details.PrescriptionDetailsRequest;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.example.health_management.domain.entities.Prescription}
 */
@Builder
@Data
public class PrescriptionRequest implements Serializable {
    private String diagnosis;
    private String notes;
    private Set<PrescriptionDetailsRequest> details;
    private Set<MedicalConditionRequest> medicalConditions;
}