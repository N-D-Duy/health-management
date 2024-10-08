package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.appointment.request.CreateAppointmentRequest;
import com.example.health_management.application.DTOs.appointment.response.AppointmentResponse;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.mapper.AppointmentMapper;
import com.example.health_management.domain.entities.Appointment;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.HealthProvider;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AppointmentRepository;
import com.example.health_management.domain.repositories.DoctorRepository;
import com.example.health_management.domain.repositories.HealthProviderRepository;
import com.example.health_management.domain.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final HealthProviderRepository healthProviderRepository;
    private UserRepository userRepository;
    private final JwtProvider jwtService;

    private DoctorRepository doctorRepository;


    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper, HealthProviderRepository healthProviderRepository, JwtProvider jwtService) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.healthProviderRepository = healthProviderRepository;
        this.jwtService = jwtService;
    }


    public AppointmentResponse createAppointment(CreateAppointmentRequest createAppointmentRequest) {
        try {
            HealthProvider healthProvider = healthProviderRepository.findById(createAppointmentRequest.getHealthProviderId())
                    .orElseThrow(() -> new RuntimeException("HealthProvider not found"));
            Doctor doctor = doctorRepository.findById(createAppointmentRequest.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor not found"));
            User user = jwtService.extractUserFromToken();
            Appointment appointment = appointmentMapper.toEntity(createAppointmentRequest, healthProvider, user, doctor);
            return appointmentMapper.toAppointmentResponseDto(appointmentRepository.save(appointment));
        } catch (RuntimeException e) {
            log.error("Create appointment error: ", e);
            throw new RuntimeException(e);
        }
    }

    public AppointmentResponse getAppointmentById(Long id) {
        try {
            return appointmentMapper.toAppointmentResponseDto(appointmentRepository.findByIdAndUser_Id(id, jwtService.extractUserFromToken().getId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found")));
        } catch (RuntimeException e) {
            log.error("Get appointment by id error: ", e);
            throw new RuntimeException(e);
        }
    }

    public List<AppointmentResponse> getAllAppointments() {
        try {
            User user = jwtService.extractUserFromToken();
            return appointmentRepository.findByUser_Id(user.getId()).stream()
                    .map(appointmentMapper::toAppointmentResponseDto)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            log.error("Get all appointments error: ", e);
            throw new RuntimeException(e);
        }
    }

    public String deleteAppointment(Long id) {
        try {
            appointmentRepository.findByIdAndUser_Id(id, jwtService.extractUserFromToken().getId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            appointmentRepository.deleteById(id);
            return "Appointment has been deleted !!!";
        } catch (RuntimeException e) {
            log.error("Delete appointment error: ", e);
            throw new RuntimeException(e);
        }
    }

    public AppointmentResponse updateAppointment(Long id, CreateAppointmentRequest createAppointmentRequest) {
        try {
            User user = jwtService.extractUserFromToken();
            Appointment appointment = appointmentRepository.findByIdAndUser_Id(id, jwtService.extractUserFromToken().getId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
                        HealthProvider healthProvider = healthProviderRepository.findById(createAppointmentRequest.getHealthProviderId())
                    .orElseThrow(() -> new RuntimeException("HealthProvider not found"));
            Doctor doctor = doctorRepository.findById(createAppointmentRequest.getDoctorId()).orElseThrow(() -> new RuntimeException("Doctor not found"));
            appointmentMapper.partialUpdateFromAppointmentRequestDto(createAppointmentRequest, healthProvider,user,doctor, appointment);
            return appointmentMapper.toAppointmentResponseDto(appointmentRepository.save(appointment));
        } catch (RuntimeException e) {
            log.error("Update appointment error: ", e);
            throw new RuntimeException(e);
        }
    }
}
