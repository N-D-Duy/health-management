package com.example.health_management.application.DTOs.doctor;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DoctorScheduleDTO {
    private Long id;
    private LocalDateTime startTime;
    private String patientName;
    private String examinationType;
    private String appointmentStatus;
    private String note;
    private Long doctorId;
}
