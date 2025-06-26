package com.example.health_management.application.DTOs.appointment_record.request;

import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.enums.AppointmentType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AppointmentRecordRequestDTO implements Serializable {
    private String note;
    private Long userId;
    private Long doctorId;
    private Long healthProviderId;
    private AppointmentType appointmentType;
    private LocalDateTime scheduledAt;
    private AppointmentStatus status;
}