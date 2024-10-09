package com.example.health_management.application.DTOs.prescription_details;

import lombok.Data;

@Data
public class PrescriptionDetailsRequest {
    private Long medicationId;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;
}
