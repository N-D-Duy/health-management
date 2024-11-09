package com.example.health_management.application.DTOs.doctor;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DoctorScheduleDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer currentPatientCount;
    private Long doctorId;
}
