package com.example.health_management.api.controllers;


import com.example.health_management.application.DTOs.medical_record.CreateAppointmentRecordDto;
import com.example.health_management.application.DTOs.medical_record.AppointmentRecordResponseDto;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.domain.services.AppointmentRecordService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical-record")
@Tag(name = "Medical Record", description = "Medical Record API")
@SecurityRequirement(name = "bearerAuth")
public class AppointmentRecordController {
    //todo: add userId as the criteria for fetching prescription as well

    private AppointmentRecordService AppointmentRecordService;

    @Autowired
    public AppointmentRecordController(AppointmentRecordService AppointmentRecordService) {
        this.AppointmentRecordService = AppointmentRecordService;
    }
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_DOCTOR'})")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AppointmentRecordResponseDto>>> getAllAppointmentRecords(@RequestParam(value = "userId", required = false) Long userId) {
        List<AppointmentRecordResponseDto> AppointmentRecordResponseDto = AppointmentRecordService.getAllAppointmentRecords(userId);
        ApiResponse<List<AppointmentRecordResponseDto>> apiResponse = ApiResponse.<List<AppointmentRecordResponseDto>>builder().code(HttpStatus.OK.value()).data(AppointmentRecordResponseDto).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_DOCTOR'})")
    @GetMapping()
    public ResponseEntity<ApiResponse<AppointmentRecordResponseDto>> getAppointmentRecordById(@RequestParam(value = "appointmentId") Long appointmentId) {
        AppointmentRecordResponseDto AppointmentRecordResponseDto = AppointmentRecordService.getAppointmentRecordByAppointmentId(appointmentId);
        ApiResponse<AppointmentRecordResponseDto> apiResponse = ApiResponse.<AppointmentRecordResponseDto>builder().code(HttpStatus.OK.value()).data(AppointmentRecordResponseDto).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AppointmentRecordResponseDto>> createAppointmentRecord(@RequestBody CreateAppointmentRecordDto appointmentRecordRequestDto) {
        AppointmentRecordResponseDto AppointmentRecordResponseDto = AppointmentRecordService.createAppointmentRecord(appointmentRecordRequestDto);
        ApiResponse<AppointmentRecordResponseDto> apiResponse = ApiResponse.<AppointmentRecordResponseDto>builder().code(HttpStatus.OK.value()).data(AppointmentRecordResponseDto).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<AppointmentRecordResponseDto>> updateAppointmentRecord(@RequestBody CreateAppointmentRecordDto appointmentRecordRequestDto) {
        AppointmentRecordResponseDto AppointmentRecordResponseDto = AppointmentRecordService.updateAppointmentRecord(appointmentRecordRequestDto);
        ApiResponse<AppointmentRecordResponseDto> apiResponse = ApiResponse.<AppointmentRecordResponseDto>builder().code(HttpStatus.OK.value()).data(AppointmentRecordResponseDto).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteAppointmentRecord(@PathVariable(value = "appointmentId") Long appointmentId) {
        String result = AppointmentRecordService.deleteAppointmentRecord(appointmentId);
        ApiResponse<String> apiResponse = ApiResponse.<String>builder().code(HttpStatus.OK.value()).data(result).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }
}
