package com.example.health_management.application.DTOs.medical_record;

import com.example.health_management.domain.entities.MedicalRecord;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link MedicalRecord}
 */
@Builder
@Value
public class CreateMedicalRecordDto implements Serializable {
    String note;
    int prescriptionId;
    Integer userId;
    int appointmentId;
}