package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.doctor.DoctorScheduleDTO;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.domain.services.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public @ResponseBody ResponseEntity<ApiResponse<DoctorScheduleDTO>> createDoctorSchedule(@RequestBody DoctorScheduleDTO doctorScheduleDTO) {
        DoctorScheduleDTO doctorSchedule = doctorScheduleService.createDoctorSchedule(doctorScheduleDTO);
        ApiResponse<DoctorScheduleDTO> response = ApiResponse.<DoctorScheduleDTO>builder().code(200).data(doctorSchedule).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public @ResponseBody ResponseEntity<ApiResponse<DoctorScheduleDTO>> updateDoctorSchedule(@RequestBody DoctorScheduleDTO doctorScheduleDTO) {
        DoctorScheduleDTO doctorSchedule = doctorScheduleService.updateDoctorSchedule(doctorScheduleDTO);
        ApiResponse<DoctorScheduleDTO> response = ApiResponse.<DoctorScheduleDTO>builder().code(200).data(doctorSchedule).message("Success").build();
        return ResponseEntity.ok(response);
    }
}
