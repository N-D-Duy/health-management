package com.example.health_management.application.DTOs.appointment_record.request;

import com.example.health_management.domain.entities.AppointmentRecord;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link AppointmentRecord}
 */
@Data
public class CreateAppointmentRecord implements Serializable {
    private String note;
    private Long prescriptionId;
    private Long userId;
    private Long appointmentId;
}