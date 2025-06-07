package com.example.health_management.application.DTOs.doctor;

import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.domain.entities.AppointmentRecord;
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
    private AppointmentStatus appointmentStatus;
    private String note;
    private Long doctorId;
    private AppointmentRecord appointmentRecord;
}
