package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.doctor.DoctorDTO;
import com.example.health_management.application.DTOs.doctor.DoctorScheduleDTO;
import com.example.health_management.application.DTOs.logging.LoggingDTO;
import com.example.health_management.application.mapper.DoctorMapper;
import com.example.health_management.application.mapper.DoctorScheduleMapper;
import com.example.health_management.common.shared.enums.LoggingType;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.DoctorSchedule;
import com.example.health_management.domain.repositories.DoctorRepository;
import com.example.health_management.domain.repositories.DoctorScheduleRepository;
import com.example.health_management.domain.repositories.HealthProviderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorScheduleService {
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleMapper doctorScheduleMapper;
    private final LoggingService loggingService;

    public DoctorScheduleDTO createDoctorSchedule(DoctorScheduleDTO doctorScheduleDTO) {
        Doctor doctor = doctorRepository.findByIdActive(doctorScheduleDTO.getDoctorId());
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found");
        }
        DoctorSchedule doctorSchedule = new DoctorSchedule();
        doctorSchedule.setDoctor(doctor);
        doctorSchedule.setStartTime(doctorScheduleDTO.getStartTime());
        doctorSchedule.setEndTime(doctorScheduleDTO.getEndTime());
        doctorSchedule.setCurrentPatientCount(doctorScheduleDTO.getCurrentPatientCount());
        loggingService.saveLog(LoggingDTO.builder()
                .type(LoggingType.DOCTOR_SCHEDULE_CREATED)
                .message("Schedule created for doctor: " + doctorScheduleDTO.getDoctorId())
                .build());

        return doctorScheduleMapper.toDTO(doctorScheduleRepository.save(doctorSchedule));
    }

    public List<DoctorScheduleDTO> getDoctorSchedules(Long doctorId) {
        List<DoctorSchedule> doctorSchedules = doctorScheduleRepository.findAllByDoctorId(doctorId);
        return doctorSchedules.stream().map(doctorScheduleMapper::toDTO).toList();
    }

    public DoctorScheduleDTO updateDoctorSchedule(DoctorScheduleDTO doctorScheduleDTO) {
        loggingService.saveLog(LoggingDTO.builder()
                .type(LoggingType.DOCTOR_SCHEDULE_UPDATED)
                .message("Schedule updated for doctor: " + doctorScheduleDTO.getDoctorId())
                .build());
        return doctorScheduleMapper.toDTO(doctorScheduleRepository.save(doctorScheduleMapper.toEntity(doctorScheduleDTO)));
    }
}
