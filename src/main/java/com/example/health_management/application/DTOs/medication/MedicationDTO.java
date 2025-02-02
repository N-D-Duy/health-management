package com.example.health_management.application.DTOs.medication;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicationDTO {
    private Long id;
    private String name;
    private String image_url;
    private String description;
    private LocalDate mfgDate;
    private LocalDate expDate;
}
