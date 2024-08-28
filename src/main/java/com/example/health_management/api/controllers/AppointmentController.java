package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.appointment.AppointmentResponseDto;
import com.example.health_management.application.DTOs.appointment.CreateAppointmentRequestDto;
import com.example.health_management.domain.services.AppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
@PreAuthorize("hasRole('ROLE_USER')")
@SecurityRequirement(name = "bearerAuth")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<AppointmentResponseDto> createAppointment(@RequestBody CreateAppointmentRequestDto createAppointmentRequestDto) {
        AppointmentResponseDto appointmentResponseDto = appointmentService.createAppointment(createAppointmentRequestDto);
        return ResponseEntity.ok(appointmentResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Integer id) {
        AppointmentResponseDto appointmentResponseDto = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointmentResponseDto);
    }
    // Get all appointments by user id
    @GetMapping("/all")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        List<AppointmentResponseDto> appointmentResponseDto = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointmentResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(@PathVariable Integer id, @RequestBody CreateAppointmentRequestDto createAppointmentRequestDto) {
        AppointmentResponseDto appointmentResponseDto = appointmentService.updateAppointment(id, createAppointmentRequestDto);
        return ResponseEntity.ok(appointmentResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Integer id) {
        String result = appointmentService.deleteAppointment(id);
        ///todo: nam : hard code message for now
        return ResponseEntity.ok(result);
    }

}
