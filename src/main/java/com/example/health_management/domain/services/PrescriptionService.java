package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.appointment_record.request.UpdateAppointmentRequestDTO;
import com.example.health_management.domain.entities.AppointmentRecord;
import com.example.health_management.domain.entities.Prescription;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PrescriptionService {
    private final PrescriptionDetailsService prescriptionDetailsService;
    private final MedicalConditionService medicalConditionService;

    public void updatePrescription(@NonNull AppointmentRecord appointmentRecord, @NonNull UpdateAppointmentRequestDTO request) {
        Prescription prescription = appointmentRecord.getPrescription();
        if (request.getPrescription() == null) {
            return;
        }

        if (prescription == null) {
            prescription = new Prescription();
            prescription.setAppointmentRecord(appointmentRecord);
        }

        prescription.setDetails(prescriptionDetailsService.updatePrescriptionDetails(prescription, request.getPrescription().getDetails()));
        prescription.setMedicalConditions(medicalConditionService.updateMedicalConditions(prescription, request.getPrescription().getMedicalConditions()));
        appointmentRecord.setPrescription(prescription);
    }

}
