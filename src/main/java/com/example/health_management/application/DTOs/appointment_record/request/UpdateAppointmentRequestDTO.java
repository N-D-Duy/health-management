package com.example.health_management.application.DTOs.appointment_record.request;

import com.example.health_management.application.DTOs.prescription.PrescriptionDTO;
import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.enums.AppointmentType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UpdateAppointmentRequestDTO implements Serializable {
    private Long id;
    private String note;
    private Long userId;
    private Long doctorId;
    private Long healthProviderId;
    private PrescriptionDTO prescription;
    private AppointmentType appointmentType;
    private LocalDateTime scheduledAt;
    private AppointmentStatus status;
}
