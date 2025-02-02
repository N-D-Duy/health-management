package com.example.health_management.application.DTOs.doctor;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DoctorScheduleDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer currentPatientCount;
    private Long doctorId;
}
