package com.example.health_management.application.DTOs.prescription_details;

import com.example.health_management.application.DTOs.medication.MedicationDTO;
import lombok.Data;

@Data
public class PrescriptionDetailsDTO {
    private Long id;
    private MedicationDTO medication;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;
}
