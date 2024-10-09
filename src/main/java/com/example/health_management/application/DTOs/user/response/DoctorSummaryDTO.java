package com.example.health_management.application.DTOs.user.response;

import com.example.health_management.application.DTOs.heath_provider.HealthProviderSummary;

public class DoctorSummaryDTO extends UserSummaryDTO {
    private String specialization;
    private Double experience;
    private String qualification;
    private Double rating;
    private String about;
}
