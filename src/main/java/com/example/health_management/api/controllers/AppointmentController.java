//package com.example.health_management.api.controllers;
//
//import com.example.health_management.application.DTOs.appointment.AppointmentResponseDto;
//import com.example.health_management.application.DTOs.appointment.CreateAppointmentRequestDto;
//import com.example.health_management.application.apiresponse.ApiResponse;
//import com.example.health_management.domain.services.AppointmentService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.access.prepost.PreFilter;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/appointment")
//@PreAuthorize("hasRole('ROLE_USER')")
//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Appointment", description = "Appointment API")
//public class AppointmentController {
//
//    private final AppointmentService appointmentService;
//
//    @Autowired
//    public AppointmentController(AppointmentService appointmentService) {
//        this.appointmentService = appointmentService;
//    }
//
//    @PostMapping("/create")
//    public ResponseEntity<ApiResponse<AppointmentResponseDto>> createAppointment(@RequestBody CreateAppointmentRequestDto createAppointmentRequestDto) {
//        AppointmentResponseDto appointmentResponseDto = appointmentService.createAppointment(createAppointmentRequestDto);
//        ApiResponse<AppointmentResponseDto> apiResponse = ApiResponse.<AppointmentResponseDto>builder().code(HttpStatus.OK.value()).data(appointmentResponseDto).message("Success").build();
//        return ResponseEntity.ok(apiResponse);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<AppointmentResponseDto>> getAppointmentById(@PathVariable Long id) {
//        AppointmentResponseDto appointmentResponseDto = appointmentService.getAppointmentById(id);
//        ApiResponse<AppointmentResponseDto> apiResponse = ApiResponse.<AppointmentResponseDto>builder().code(HttpStatus.OK.value()).data(appointmentResponseDto).message("Success").build();
//        return ResponseEntity.ok(apiResponse);
//    }
//    // Get all appointments by user id
//    @GetMapping("/all")
//    public ResponseEntity<ApiResponse<List<AppointmentResponseDto>>> getAllAppointments() {
//        List<AppointmentResponseDto> appointmentResponseDto = appointmentService.getAllAppointments();
//        ApiResponse<List<AppointmentResponseDto>> apiResponse = ApiResponse.<List<AppointmentResponseDto>>builder().code(HttpStatus.OK.value()).data(appointmentResponseDto).message("Success").build();
//        return ResponseEntity.ok(apiResponse);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse<AppointmentResponseDto>> updateAppointment(@PathVariable Long id, @RequestBody CreateAppointmentRequestDto createAppointmentRequestDto) {
//        AppointmentResponseDto appointmentResponseDto = appointmentService.updateAppointment(id, createAppointmentRequestDto);
//        ApiResponse<AppointmentResponseDto> apiResponse = ApiResponse.<AppointmentResponseDto>builder().code(HttpStatus.OK.value()).data(appointmentResponseDto).message("Success").build();
//        return ResponseEntity.ok(apiResponse);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse> deleteAppointment(@PathVariable Long id) {
//        String result = appointmentService.deleteAppointment(id);
//        ApiResponse apiResponse = ApiResponse.builder().code(HttpStatus.OK.value()).message(result).build();
//        return ResponseEntity.ok(apiResponse);
//    }
//
//}
