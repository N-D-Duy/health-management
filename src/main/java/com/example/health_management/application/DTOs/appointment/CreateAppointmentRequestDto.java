package com.example.health_management.application.DTOs.appointment;

import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.enums.AppointmentType;
import com.example.health_management.domain.entities.Appointment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Appointment}
 */
@Value
@Builder
@Getter @Setter
public class CreateAppointmentRequestDto implements Serializable {
    LocalDate date;
    Long healthProviderId;
    Long doctorId;
    String note;
    AppointmentStatus status;
    AppointmentType appointmentType;
}