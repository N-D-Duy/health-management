package com.example.health_management.application.DTOs.appointment_record.request;

import com.example.health_management.application.DTOs.prescription.PrescriptionRequest;
import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.enums.AppointmentType;
import com.example.health_management.domain.entities.AppointmentRecord;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link AppointmentRecord}
 */
@Data
public class CreateAppointmentRecordRequest implements Serializable {
    private String note;
    private PrescriptionRequest prescription;
    private Long userId;
    private Long doctorId;
    private Long healthProviderId;
    private AppointmentType appointmentType;
    private LocalDate scheduledAt;
    private AppointmentStatus status;
}