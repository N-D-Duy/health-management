package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.appointment_record.request.UpdateAppointmentRequestDTO;
import com.example.health_management.application.mapper.PrescriptionMapper;
import com.example.health_management.domain.entities.AppointmentRecord;
import com.example.health_management.domain.entities.Prescription;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PrescriptionService {
    private final PrescriptionDetailsService prescriptionDetailsService;
    private final MedicalConditionService medicalConditionService;
    public void updatePrescription(@NonNull AppointmentRecord appointmentRecord, @NonNull UpdateAppointmentRequestDTO request) {
        int count = 0;
        if (request.getPrescription() == null) {
            return;
        }
        Prescription prescription = appointmentRecord.getPrescription();

        if (prescription == null) {
            prescription = new Prescription();
            prescription.setAppointmentRecord(appointmentRecord);
        }

        if(request.getPrescription().getDetails() != null){
            count++;
            prescription.setDetails(prescriptionDetailsService.updatePrescriptionDetails(prescription, request.getPrescription().getDetails()));
        }
        if(request.getPrescription().getMedicalConditions() != null) {
            count++;
            prescription.setMedicalConditions(medicalConditionService.updateMedicalConditions(prescription, request.getPrescription().getMedicalConditions()));
        }
        if(count > 0){
            appointmentRecord.setPrescription(prescription);
        }
    }
}
