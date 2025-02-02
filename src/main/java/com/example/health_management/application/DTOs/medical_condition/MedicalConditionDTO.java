package com.example.health_management.application.DTOs.medical_condition;

import com.example.health_management.common.shared.enums.MedicalConditionStatus;
import lombok.Data;

@Data
public class MedicalConditionDTO {
    private Long id;
    private String conditionName;
    private MedicalConditionStatus status;
    private String notes;
}
