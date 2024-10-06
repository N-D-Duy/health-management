package com.example.health_management.application.DTOs.appointment.request;

import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.enums.AppointmentType;
import com.example.health_management.domain.entities.Appointment;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Appointment}
 */
@Data
public class CreateAppointmentRequest implements Serializable {
    private LocalDate date;
    private Long healthProviderId;
    private Long doctorId;
    private String note;
    private AppointmentStatus status;
    private AppointmentType appointmentType;
}