package com.example.health_management.application.DTOs.user.request;

import com.example.health_management.common.shared.enums.DoctorSpecialization;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class UpdateDoctorRequest{
    private DoctorSpecialization specialization;
    private Double experience;
    private String qualification;
    private Double rating;
    private String about;
}
