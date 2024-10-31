package com.example.health_management.application.DTOs.analysis;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class DataAnalysis implements Serializable {
    private Long totalUsers;
    private Long totalDoctors;
    private Long totalPatients;
    private List<Map<String, Long>> gender;
    private Long appointmentsScheduled;
    private Long appointmentsCompleted;
    private Long appointmentsCancelled;
    private Long articlesCreated;
}
