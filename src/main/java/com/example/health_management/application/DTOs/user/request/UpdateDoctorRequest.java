package com.example.health_management.application.DTOs.user.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class UpdateDoctorRequest{
    private String specialization;
    private Double experience;
    private String qualification;
    private Double rating;
    private String about;
}
