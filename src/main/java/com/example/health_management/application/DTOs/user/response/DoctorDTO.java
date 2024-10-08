package com.example.health_management.application.DTOs.user.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class DoctorDTO{
    private Long id;
    private String specialization;
    private Double experience;
    private String qualification;
    private Double rating;
    private String about;
}
