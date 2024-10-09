package com.example.health_management.domain.services;

import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.mapper.AppointmentRecordMapper;
import com.example.health_management.domain.repositories.AppointmentRecordRepository;
import com.example.health_management.domain.repositories.PrescriptionRepository;
import com.example.health_management.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentRecordService {
    private final AppointmentRecordRepository appointmentRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final UserRepository userRepository;

    private final AppointmentRecordMapper appointmentRecordMapper;

    private final JwtProvider jwtService;



    public String deleteAppointmentRecord(Long appointmentRecordId) {
        try {
            appointmentRecordRepository.deleteById(appointmentRecordId); //soft delete
            return "Appointment Record deleted successfully";
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
