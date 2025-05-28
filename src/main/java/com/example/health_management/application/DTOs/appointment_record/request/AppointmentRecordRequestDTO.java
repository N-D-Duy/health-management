package com.example.health_management.application.DTOs.appointment_record.request;

import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.enums.AppointmentType;
import com.example.health_management.common.shared.enums.PaymentStatus;
import com.example.health_management.domain.entities.AppointmentRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
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
    private PaymentStatus paymentStatus;
}