package com.example.health_management.application.DTOs.appointment_record.response;

import com.example.health_management.application.DTOs.heath_provider.HealthProviderSummary;
import com.example.health_management.application.DTOs.prescription.PrescriptionDTO;
import com.example.health_management.application.DTOs.doctor.DoctorSummaryDTO;
import com.example.health_management.application.DTOs.user.response.UserSummaryDTO;
import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.enums.AppointmentType;
import com.example.health_management.domain.entities.AppointmentRecord;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link AppointmentRecord}
 */
@Data
public class AppointmentRecordDTO implements Serializable {
    private Long id;
    private String note;
    private PrescriptionDTO prescription;
    private UserSummaryDTO user;
    private DoctorSummaryDTO doctor;
    private HealthProviderSummary healthProvider;
    private AppointmentType appointmentType;
    private LocalDateTime scheduledAt;
    private AppointmentStatus status;
}