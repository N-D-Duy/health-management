package com.example.health_management.application.DTOs.doctor;

import com.example.health_management.common.shared.enums.DoctorSpecialization;
import lombok.Data;

@Data
public class DoctorDTO{
    private Long id;
    private DoctorSpecialization specialization;
    private Double experience;
    private String qualification;
    private Double rating;
    private String about;
}
