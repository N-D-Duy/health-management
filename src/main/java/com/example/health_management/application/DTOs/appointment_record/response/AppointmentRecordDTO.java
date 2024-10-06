package com.example.health_management.application.DTOs.appointment_record.response;

import com.example.health_management.application.DTOs.appointment.response.AppointmentResponse;
import com.example.health_management.application.DTOs.prescription.PrescriptionDTO;
import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.example.health_management.domain.entities.AppointmentRecord;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link AppointmentRecord}
 */
@Data
public class AppointmentRecordDTO implements Serializable {
    private int id;
    private String note;
    private PrescriptionDTO prescription;
    private UserDTO user;
    private AppointmentResponse appointment;
}