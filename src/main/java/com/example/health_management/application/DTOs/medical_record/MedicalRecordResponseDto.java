package com.example.health_management.application.DTOs.medical_record;

import com.example.health_management.application.DTOs.appointment.AppointmentResponseDto;
import com.example.health_management.application.DTOs.prescription.PrescriptionResponseDto;
import com.example.health_management.application.DTOs.user.UserResponseDto;
import com.example.health_management.domain.entities.MedicalRecord;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link MedicalRecord}
 */
@Builder
@Getter
@Value
public class MedicalRecordResponseDto implements Serializable {
    int id;
    String note;
    @JsonIgnoreProperties({"user"})
    PrescriptionResponseDto prescription;
    UserResponseDto user;
    @JsonIgnoreProperties({"user"})
    AppointmentResponseDto appointment;
}