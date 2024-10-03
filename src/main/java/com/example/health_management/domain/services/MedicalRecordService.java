package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.medical_record.CreateMedicalRecordDto;
import com.example.health_management.application.DTOs.medical_record.MedicalRecordResponseDto;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.mapper.medical_record.MedicalRecordMapper;
import com.example.health_management.common.shared.enums.Role;
import com.example.health_management.domain.entities.Appointment;
import com.example.health_management.domain.entities.MedicalRecord;
import com.example.health_management.domain.entities.Prescription;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AppointmentRepository;
import com.example.health_management.domain.repositories.MedicalRecordRepository;
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
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    private final MedicalRecordMapper medicalRecordMapper;

    private final JwtProvider jwtService;


    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, PrescriptionRepository prescriptionRepository, UserRepository userRepository, AppointmentRepository appointmentRepository, MedicalRecordMapper medicalRecordMapper, JwtProvider jwtService) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.medicalRecordMapper = medicalRecordMapper;
        this.jwtService = jwtService;
    }


    public List<MedicalRecordResponseDto> getAllMedicalRecords(@Nullable Integer userId) {
        try {
            Role role = jwtService.extractUserFromToken().getAccount().getRole();
            userId = (role.equals(Role.DOCTOR) && userId != null) ? userId : jwtService.extractUserFromToken().getId();
            if(role.equals(Role.DOCTOR)){
                userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            }
            return medicalRecordRepository.findByUser_Id(userId).stream().map(medicalRecordMapper::toMedicalRecordResponseDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public MedicalRecordResponseDto getMedicalRecordByAppointmentId(Integer appointmentId) {
        try {
            return medicalRecordMapper.toMedicalRecordResponseDto(medicalRecordRepository.findByAppointment_IdAndUser_Id(appointmentId, jwtService.extractUserFromToken().getId()).orElseThrow(() -> new RuntimeException("Medical Record not found")));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public MedicalRecordResponseDto createMedicalRecord(CreateMedicalRecordDto medicalRecordRequestDto) {
        try {
            medicalRecordRepository.findByAppointment_Id(medicalRecordRequestDto.getAppointmentId()).ifPresent(medicalRecord -> {
                throw new RuntimeException("Medical Record already exists");
            });
            User user = userRepository.findById(medicalRecordRequestDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
           Prescription prescription = prescriptionRepository.findByUser_IdAndId(medicalRecordRequestDto.getUserId(),medicalRecordRequestDto.getPrescriptionId()).orElseThrow(() -> new RuntimeException("Prescription not found"));
           Appointment appointment = appointmentRepository.findByIdAndUser_Id(medicalRecordRequestDto.getAppointmentId(), medicalRecordRequestDto.getUserId()).orElseThrow(() -> new RuntimeException("Appointment not found"));
           MedicalRecord medicalRecord = medicalRecordMapper.toMedicalRecord(medicalRecordRequestDto, prescription, user, appointment);
           return medicalRecordMapper.toMedicalRecordResponseDto(medicalRecordRepository.save(medicalRecord));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public MedicalRecordResponseDto updateMedicalRecord(CreateMedicalRecordDto medicalRecordRequestDto) {
        try {
            MedicalRecord medicalRecord = medicalRecordRepository.findByAppointment_Id(medicalRecordRequestDto.getAppointmentId()).orElseThrow(() -> new RuntimeException("Medical Record not found"));
            User user = userRepository.findById(medicalRecordRequestDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            Prescription prescription = prescriptionRepository.findByUser_IdAndId(medicalRecordRequestDto.getUserId(),medicalRecordRequestDto.getPrescriptionId()).orElseThrow(() -> new RuntimeException("Prescription not found"));
            Appointment appointment = appointmentRepository.findByIdAndUser_Id(medicalRecordRequestDto.getAppointmentId(), medicalRecordRequestDto.getUserId()).orElseThrow(() -> new RuntimeException("Appointment not found"));            medicalRecord = medicalRecordMapper.partialUpdate(medicalRecord, medicalRecordRequestDto, user, prescription, appointment);
            return medicalRecordMapper.toMedicalRecordResponseDto(medicalRecordRepository.save(medicalRecord));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String deleteMedicalRecord(Integer appointmentId) {
        try {
            MedicalRecord medicalRecord = medicalRecordRepository.findByAppointment_Id(appointmentId).orElseThrow(() -> new RuntimeException("Medical Record not found"));
            medicalRecordRepository.delete(medicalRecord);
            return "Medical Record deleted successfully";
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
