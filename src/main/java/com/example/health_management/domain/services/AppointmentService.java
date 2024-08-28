package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.appointment.AppointmentResponseDto;
import com.example.health_management.application.DTOs.appointment.CreateAppointmentRequestDto;
import com.example.health_management.application.mapper.appointment.AppointmentMapper;
import com.example.health_management.domain.entities.Appointment;
import com.example.health_management.domain.entities.AppointmentType;
import com.example.health_management.domain.entities.HealthProvider;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppointmentService {

    private AppointmentRepository appointmentRepository;
    private AppointmentTypeRepository appointmentTypeRepository;
    private AppointmentMapper appointmentMapper;
    private HealthProviderRepository healthProviderRepository;
    private CountryRepository countryRepository;
    private UserRepository userRepository;
    private JwtService jwtService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, AppointmentMapper appointmentMapper, HealthProviderRepository healthProviderRepository, CountryRepository countryRepository, AppointmentTypeRepository appointmentTypeRepository, JwtService jwtService) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.healthProviderRepository = healthProviderRepository;
        this.countryRepository = countryRepository;
        this.appointmentTypeRepository = appointmentTypeRepository;
        this.jwtService = jwtService;
    }


    public AppointmentResponseDto createAppointment(CreateAppointmentRequestDto createAppointmentRequestDto) {
        try {
            HealthProvider healthProvider = healthProviderRepository.findById(createAppointmentRequestDto.getHealthProviderId())
                    .orElseThrow(() -> new RuntimeException("HealthProvider not found"));
            AppointmentType appointmentType = appointmentTypeRepository.findById(createAppointmentRequestDto.getAppointmentTypeId())
                    .orElseThrow(() -> new RuntimeException("AppointmentType not found"));
            User user = jwtService.extractUserFromToken();
            Appointment appointment = appointmentMapper.toEntity(createAppointmentRequestDto, healthProvider, appointmentType, user);
            return appointmentMapper.toAppointmentResponseDto(appointmentRepository.save(appointment));
        } catch (RuntimeException e) {
            log.error("Create appointment error: ", e);
            throw new RuntimeException(e);
        }
    }

    public AppointmentResponseDto getAppointmentById(Integer id) {
        try {
            return appointmentMapper.toAppointmentResponseDto(appointmentRepository.findByIdAndUser_Id(id, jwtService.extractUserFromToken().getId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found")));
        } catch (RuntimeException e) {
            log.error("Get appointment by id error: ", e);
            throw new RuntimeException(e);
        }
    }

    public List<AppointmentResponseDto> getAllAppointments() {
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

    public String deleteAppointment(Integer id) {
        try {
            Appointment appointment = appointmentRepository.findByIdAndUser_Id(id, jwtService.extractUserFromToken().getId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            appointmentRepository.deleteById(id);
            return "Appointment has been deleted !!!";
        } catch (RuntimeException e) {
            log.error("Delete appointment error: ", e);
            throw new RuntimeException(e);
        }
    }

    public AppointmentResponseDto updateAppointment(Integer id, CreateAppointmentRequestDto createAppointmentRequestDto) {
        try {

//            User user = jwtService.extractUserFromToken();
            Appointment appointment = appointmentRepository.findByIdAndUser_Id(id, jwtService.extractUserFromToken().getId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
                        HealthProvider healthProvider = healthProviderRepository.findById(createAppointmentRequestDto.getHealthProviderId())
                    .orElseThrow(() -> new RuntimeException("HealthProvider not found"));
            AppointmentType appointmentType = appointmentTypeRepository.findById(createAppointmentRequestDto.getAppointmentTypeId())
                    .orElseThrow(() -> new RuntimeException("AppointmentType not found"));
            appointmentMapper.partialUpdateFromAppointmentRequestDto(createAppointmentRequestDto, healthProvider, appointmentType, appointment);
            return appointmentMapper.toAppointmentResponseDto(appointmentRepository.save(appointment));
        } catch (RuntimeException e) {
            log.error("Update appointment error: ", e);
            throw new RuntimeException(e);
        }
    }
}
