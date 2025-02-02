package com.example.health_management.application.DTOs.doctor;

import com.example.health_management.common.shared.enums.DoctorSpecialization;
import lombok.Data;

@Data
public class UpdateDoctorRequest{
    private DoctorSpecialization specialization;
    private Double experience;
    private String qualification;
    private Double rating;
    private String about;
}
