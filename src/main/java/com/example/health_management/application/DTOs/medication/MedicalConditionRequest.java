package com.example.health_management.application.DTOs.medication;

import com.example.health_management.common.shared.enums.MedicalConditionStatus;
import lombok.Data;

@Data
public class MedicalConditionRequest {
    private String condition_name;
    private MedicalConditionStatus status;
    private String notes;
}
