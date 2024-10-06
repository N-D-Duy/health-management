package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.appointment_record.request.CreateAppointmentRecord;
import com.example.health_management.application.DTOs.appointment_record.response.AppointmentRecordDTO;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.mapper.AppointmentRecordMapper;
import com.example.health_management.common.shared.enums.Role;
import com.example.health_management.domain.entities.Appointment;
import com.example.health_management.domain.entities.AppointmentRecord;
import com.example.health_management.domain.entities.Prescription;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AppointmentRepository;
import com.example.health_management.domain.repositories.AppointmentRecordRepository;
import com.example.health_management.domain.repositories.PrescriptionRepository;
import com.example.health_management.domain.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class AppointmentRecordService {
    private final AppointmentRecordRepository appointmentRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    private final AppointmentRecordMapper appointmentRecordMapper;

    private final JwtProvider jwtService;


    @Autowired
    public AppointmentRecordService(AppointmentRecordRepository appointmentRecordRepository, PrescriptionRepository prescriptionRepository, UserRepository userRepository, AppointmentRepository appointmentRepository, AppointmentRecordMapper appointmentRecordMapper, JwtProvider jwtService) {
        this.appointmentRecordRepository = appointmentRecordRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentRecordMapper = appointmentRecordMapper;
        this.jwtService = jwtService;
    }


    public List<AppointmentRecordDTO> getAllAppointmentRecords(@Nullable Long userId) {
        try {
            Role role = jwtService.extractUserFromToken().getAccount().getRole();
            userId = (role.equals(Role.DOCTOR) && userId != null) ? userId : jwtService.extractUserFromToken().getId();
            if(role.equals(Role.DOCTOR)){
                userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            }
            return appointmentRecordRepository.findByUser_Id(userId).stream().map(appointmentRecordMapper::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public AppointmentRecordDTO getAppointmentRecordByAppointmentId(Long appointmentId) {
        try {
            return appointmentRecordMapper.toDTO(appointmentRecordRepository.findByAppointment_IdAndUser_Id(appointmentId, jwtService.extractUserFromToken().getId()).orElseThrow(() -> new RuntimeException("Medical Record not found")));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public AppointmentRecordDTO createAppointmentRecord(CreateAppointmentRecord createAppointmentRecord) {
        try {
            appointmentRecordRepository.findByAppointment_Id(createAppointmentRecord.getAppointmentId()).ifPresent(appointmentRecord -> {
                throw new RuntimeException("Medical Record already exists");
            });
            User user = userRepository.findById(createAppointmentRecord.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
           Prescription prescription = prescriptionRepository.findByUser_IdAndId(createAppointmentRecord.getUserId(), createAppointmentRecord.getPrescriptionId()).orElseThrow(() -> new RuntimeException("Prescription not found"));
           Appointment appointment = appointmentRepository.findByIdAndUser_Id(createAppointmentRecord.getAppointmentId(), createAppointmentRecord.getUserId()).orElseThrow(() -> new RuntimeException("Appointment not found"));
           AppointmentRecord appointmentRecord = appointmentRecordMapper.toEntity(createAppointmentRecord, prescription, user, appointment);
           return appointmentRecordMapper.toDTO(appointmentRecordRepository.save(appointmentRecord));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public AppointmentRecordDTO updateAppointmentRecord(CreateAppointmentRecord createAppointmentRecord) {
        try {
            AppointmentRecord appointmentRecord = appointmentRecordRepository.findByAppointment_Id(createAppointmentRecord.getAppointmentId()).orElseThrow(() -> new RuntimeException("Medical Record not found"));
            User user = userRepository.findById(createAppointmentRecord.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            Prescription prescription = prescriptionRepository.findByUser_IdAndId(createAppointmentRecord.getUserId(), createAppointmentRecord.getPrescriptionId()).orElseThrow(() -> new RuntimeException("Prescription not found"));
            Appointment appointment = appointmentRepository.findByIdAndUser_Id(createAppointmentRecord.getAppointmentId(), createAppointmentRecord.getUserId()).orElseThrow(() -> new RuntimeException("Appointment not found"));            appointmentRecord = appointmentRecordMapper.partialUpdate(appointmentRecord, createAppointmentRecord, user, prescription, appointment);
            return appointmentRecordMapper.toDTO(appointmentRecordRepository.save(appointmentRecord));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String deleteAppointmentRecord(Long appointmentId) {
        try {
//            AppointmentRecord appointmentRecord = appointmentRecordRepository.findByAppointment_Id(appointmentId).orElseThrow(() -> new RuntimeException("Medical Record not found"));
//            appointmentRecordRepository.delete(appointmentRecord);
            appointmentRecordRepository.deleteById(appointmentId); //soft delete
            return "Medical Record deleted successfully";
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
