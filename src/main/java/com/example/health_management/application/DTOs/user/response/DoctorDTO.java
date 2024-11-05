package com.example.health_management.application.DTOs.user.response;

import com.example.health_management.common.shared.enums.DoctorSpecialization;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class DoctorDTO{
    private Long id;
    private DoctorSpecialization specialization;
    private Double experience;
    private String qualification;
    private Double rating;
    private String about;
}
