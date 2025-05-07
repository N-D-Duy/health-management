package com.example.health_management.api.controllers;


import com.example.health_management.application.DTOs.appointment_record.request.AppointmentRecordRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.request.UpdateAppointmentRequestDTO;
import com.example.health_management.application.DTOs.appointment_record.response.AppointmentRecordDTO;
import com.example.health_management.application.DTOs.medication.MedicationDTO;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.domain.services.AppointmentRecordService;
import com.example.health_management.domain.services.MedicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@Tag(name = "Appointment Record", description = "Appointment Record API")
@Slf4j
@RequiredArgsConstructor
public class AppointmentRecordController {

    private final AppointmentRecordService AppointmentRecordService;
    private final MedicationService medicationService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AppointmentRecordDTO>>> getAllAppointmentRecords() {
        List<AppointmentRecordDTO> AppointmentRecordDTO = AppointmentRecordService.findAll();
        ApiResponse<List<AppointmentRecordDTO>> apiResponse = ApiResponse.<List<AppointmentRecordDTO>>builder().code(HttpStatus.OK.value()).data(AppointmentRecordDTO).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentRecordDTO>> getAppointmentRecordById(@PathVariable(value = "id") Long appointmentId) {
        AppointmentRecordDTO AppointmentRecordDTO = AppointmentRecordService.getById(appointmentId);
        ApiResponse<AppointmentRecordDTO> apiResponse = ApiResponse.<AppointmentRecordDTO>builder().code(HttpStatus.OK.value()).data(AppointmentRecordDTO).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AppointmentRecordDTO>> createAppointmentRecord(@RequestBody AppointmentRecordRequestDTO appointmentRecordRequestDto) {
        AppointmentRecordDTO appointmentRecordDTO = AppointmentRecordService.create(appointmentRecordRequestDto);
        ApiResponse<AppointmentRecordDTO> apiResponse = ApiResponse.<AppointmentRecordDTO>builder().code(HttpStatus.OK.value()).data(appointmentRecordDTO).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

//    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<AppointmentRecordDTO>> updateAppointmentRecord(@RequestBody UpdateAppointmentRequestDTO appointmentRecordRequestDto) {
        AppointmentRecordDTO AppointmentRecordDTO = AppointmentRecordService.update(appointmentRecordRequestDto);
        ApiResponse<AppointmentRecordDTO> apiResponse = ApiResponse.<AppointmentRecordDTO>builder().code(HttpStatus.OK.value()).data(AppointmentRecordDTO).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAppointmentRecord(@PathVariable(value = "id") Long appointmentId) {
        String result = AppointmentRecordService.deleteAppointmentRecord(appointmentId);
        ApiResponse<String> apiResponse = ApiResponse.<String>builder().code(HttpStatus.OK.value()).data(result).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<List<AppointmentRecordDTO>>> getAppointmentRecordByUserId(@PathVariable(value = "id") Long userId) {
        List<AppointmentRecordDTO> AppointmentRecordDTO = AppointmentRecordService.getByUserId(userId);
        ApiResponse<List<AppointmentRecordDTO>> apiResponse = ApiResponse.<List<AppointmentRecordDTO>>builder().code(HttpStatus.OK.value()).data(AppointmentRecordDTO).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<ApiResponse<List<AppointmentRecordDTO>>> getAppointmentRecordByDoctorId(@PathVariable(value = "id") Long doctorId) {
        List<AppointmentRecordDTO> AppointmentRecordDTO = AppointmentRecordService.getByDoctorId(doctorId);
        ApiResponse<List<AppointmentRecordDTO>> apiResponse = ApiResponse.<List<AppointmentRecordDTO>>builder().code(HttpStatus.OK.value()).data(AppointmentRecordDTO).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/medication/{id}")
    public ResponseEntity<ApiResponse<MedicationDTO>> getAppointmentRecordByMedicationId(@PathVariable(value = "id") Long medicationId) {
        MedicationDTO response = medicationService.getById(medicationId);
        ApiResponse<MedicationDTO> apiResponse = ApiResponse.<MedicationDTO>builder().code(HttpStatus.OK.value()).data(response).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("medication/all")
    public ResponseEntity<ApiResponse<List<MedicationDTO>>> getAllMedications() {
        List<MedicationDTO> response = medicationService.findAll();
        ApiResponse<List<MedicationDTO>> apiResponse = ApiResponse.<List<MedicationDTO>>builder().code(HttpStatus.OK.value()).data(response).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }


    @PostMapping("medication/create")
    public ResponseEntity<ApiResponse<MedicationDTO>> createMedication(@RequestBody MedicationDTO medicationDTO) {
        MedicationDTO response = medicationService.create(medicationDTO);
        ApiResponse<MedicationDTO> apiResponse = ApiResponse.<MedicationDTO>builder().code(HttpStatus.OK.value()).data(response).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("medication/update")
    public ResponseEntity<ApiResponse<MedicationDTO>> updateMedication(@RequestBody MedicationDTO medicationDTO) {
        MedicationDTO response = medicationService.update(medicationDTO);
        ApiResponse<MedicationDTO> apiResponse = ApiResponse.<MedicationDTO>builder().code(HttpStatus.OK.value()).data(response).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }


}
