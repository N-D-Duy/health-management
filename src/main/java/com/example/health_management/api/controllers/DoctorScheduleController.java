package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.doctor.DoctorAvailableResponse;
import com.example.health_management.application.DTOs.doctor.DoctorScheduleDTO;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.domain.services.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/schedule")
@Slf4j
@RequiredArgsConstructor
public class DoctorScheduleController {
    private final DoctorScheduleService doctorScheduleService;

    @GetMapping("/doctor/{id}")
    public @ResponseBody ResponseEntity<ApiResponse<List<DoctorScheduleDTO>>> getDoctorSchedules(@PathVariable("id") Long doctorId) {
        List<DoctorScheduleDTO> doctorSchedules = doctorScheduleService.getDoctorSchedules(doctorId);
        ApiResponse<List<DoctorScheduleDTO>> response = ApiResponse.<List<DoctorScheduleDTO>>builder().code(200).data(doctorSchedules).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{doctorId}/busy-times")
    public @ResponseBody ResponseEntity<ApiResponse<List<DoctorAvailableResponse>>> getBusyTimes(@PathVariable("doctorId") Long doctorId) {
        List<DoctorAvailableResponse> availableTimes = doctorScheduleService.getBusyTimes(doctorId);
        ApiResponse<List<DoctorAvailableResponse>> response = ApiResponse.<List<DoctorAvailableResponse>>builder().code(200).data(availableTimes).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/export/{doctorId}")
    public @ResponseBody ResponseEntity<ByteArrayResource> exportDoctorSchedules(@PathVariable("doctorId") Long doctorId, @RequestParam("startDate") LocalDateTime startDate, @RequestParam(value = "endDate", required = false) LocalDateTime endDate) {
        ByteArrayResource file = doctorScheduleService.exportDoctorSchedules(doctorId, startDate, endDate);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename())
                .body(file);
    }
}
