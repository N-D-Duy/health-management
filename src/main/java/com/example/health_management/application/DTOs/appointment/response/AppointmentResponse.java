package com.example.health_management.application.DTOs.appointment.response;

import com.example.health_management.application.DTOs.heath_provider.HealthProviderDTO;
import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.example.health_management.common.shared.enums.AppointmentType;
import com.example.health_management.domain.entities.Doctor;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.example.health_management.domain.entities.Appointment}
 */
@Data
@Builder
public class AppointmentResponse implements Serializable {
    private long id;
    private LocalDate date;
    private HealthProviderDTO healthProvider;
    private UserDTO user;
    private Doctor doctor;
    private AppointmentType appointmentType;
    private String note;
}