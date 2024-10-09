//package com.example.health_management.api.controllers;
//
//import com.example.health_management.application.DTOs.appointment.request.CreateAppointmentRequest;
//import com.example.health_management.application.DTOs.appointment.response.AppointmentResponse;
//import com.example.health_management.application.apiresponse.ApiResponse;
//import com.example.health_management.domain.services.AppointmentService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
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
//    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(@RequestBody CreateAppointmentRequest createAppointmentRequest) {
//        AppointmentResponse appointmentResponse = appointmentService.createAppointment(createAppointmentRequest);
//        ApiResponse<AppointmentResponse> apiResponse = ApiResponse.<AppointmentResponse>builder().code(HttpStatus.OK.value()).data(appointmentResponse).message("Success").build();
//        return ResponseEntity.ok(apiResponse);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointmentById(@PathVariable Long id) {
//        AppointmentResponse appointmentResponse = appointmentService.getAppointmentById(id);
//        ApiResponse<AppointmentResponse> apiResponse = ApiResponse.<AppointmentResponse>builder().code(HttpStatus.OK.value()).data(appointmentResponse).message("Success").build();
//        return ResponseEntity.ok(apiResponse);
//    }
//    // Get all appointments by user id
//    @GetMapping("/all")
//    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAllAppointments() {
//        List<AppointmentResponse> appointmentResponse = appointmentService.getAllAppointments();
//        ApiResponse<List<AppointmentResponse>> apiResponse = ApiResponse.<List<AppointmentResponse>>builder().code(HttpStatus.OK.value()).data(appointmentResponse).message("Success").build();
//        return ResponseEntity.ok(apiResponse);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse<AppointmentResponse>> updateAppointment(@PathVariable Long id, @RequestBody CreateAppointmentRequest createAppointmentRequest) {
//        AppointmentResponse appointmentResponse = appointmentService.updateAppointment(id, createAppointmentRequest);
//        ApiResponse<AppointmentResponse> apiResponse = ApiResponse.<AppointmentResponse>builder().code(HttpStatus.OK.value()).data(appointmentResponse).message("Success").build();
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
