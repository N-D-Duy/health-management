package com.example.health_management.application.DTOs.health_plan;

import lombok.Data;

import java.io.Serializable;

@Data
public class HealthPlanDTO implements Serializable {
    private Long id;
    private Long userId;
    private String planType;
    private String startDate;
    private String endDate;
    private String goals;
    private String notes;
}
