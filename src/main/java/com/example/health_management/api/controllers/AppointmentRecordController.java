package com.example.health_management.api.controllers;


import com.example.health_management.application.DTOs.appointment_record.request.AppointmentRecordRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.request.UpdateAppointmentRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.response.AppointmentRecordDTO;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.domain.services.AppointmentRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment-record")
@Tag(name = "Medical Record", description = "Medical Record API")
@Slf4j
@RequiredArgsConstructor
public class AppointmentRecordController {

    private final AppointmentRecordService AppointmentRecordService;

    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_DOCTOR'})")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AppointmentRecordDTO>>> getAllAppointmentRecords() {
        List<AppointmentRecordDTO> AppointmentRecordDTO = AppointmentRecordService.findAll();
        ApiResponse<List<AppointmentRecordDTO>> apiResponse = ApiResponse.<List<AppointmentRecordDTO>>builder().code(HttpStatus.OK.value()).data(AppointmentRecordDTO).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AppointmentRecordDTO>> createAppointmentRecord(@RequestBody AppointmentRecordRequestDTO appointmentRecordRequestDto) {
        log.debug("Received request: {}", appointmentRecordRequestDto);
        AppointmentRecordDTO appointmentRecordDTO = AppointmentRecordService.create(appointmentRecordRequestDto);
        ApiResponse<AppointmentRecordDTO> apiResponse = ApiResponse.<AppointmentRecordDTO>builder().code(HttpStatus.OK.value()).data(appointmentRecordDTO).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<AppointmentRecordDTO>> updateAppointmentRecord(@RequestBody UpdateAppointmentRequestDTO appointmentRecordRequestDto) {
        AppointmentRecordDTO AppointmentRecordDTO = AppointmentRecordService.update(appointmentRecordRequestDto);
        ApiResponse<AppointmentRecordDTO> apiResponse = ApiResponse.<AppointmentRecordDTO>builder().code(HttpStatus.OK.value()).data(AppointmentRecordDTO).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAppointmentRecord(@PathVariable(value = "id") Long appointmentId) {
        String result = AppointmentRecordService.deleteAppointmentRecord(appointmentId);
        ApiResponse<String> apiResponse = ApiResponse.<String>builder().code(HttpStatus.OK.value()).data(result).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }
}
