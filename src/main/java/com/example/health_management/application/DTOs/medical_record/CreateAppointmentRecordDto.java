package com.example.health_management.application.DTOs.medical_record;

import com.example.health_management.domain.entities.AppointmentRecord;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link AppointmentRecord}
 */
@Builder
@Value
public class CreateAppointmentRecordDto implements Serializable {
    String note;
    Long prescriptionId;
    Long userId;
    Long appointmentId;
}