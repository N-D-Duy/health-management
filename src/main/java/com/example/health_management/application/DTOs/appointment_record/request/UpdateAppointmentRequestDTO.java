package com.example.health_management.application.DTOs.appointment_record.request;

import com.example.health_management.application.DTOs.prescription.PrescriptionDTO;
import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.enums.AppointmentType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UpdateAppointmentRequestDTO implements Serializable {
    private Long id;
    private String note;
    private Long userId;
    private Long doctorId;
    private Long healthProviderId;
    private PrescriptionDTO prescription;
    private AppointmentType appointmentType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate scheduledAt;
    private AppointmentStatus status;
}
