package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.prescription.CreatePrescriptionDto;
import com.example.health_management.application.DTOs.prescription.PrescriptionResponseDto;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.domain.services.PrescriptionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescription")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Prescription", description = "Prescription API")
public class PrescriptionController {

    private PrescriptionService prescriptionService;
    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }
    @PreAuthorize("hasAnyRole({'ROLE_DOCTOR', 'ROLE_USER'})")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<PrescriptionResponseDto>>> getAllPrescriptions(@RequestParam(required = false, value = "user_id") Long userId) {
        List<PrescriptionResponseDto> prescriptionResponseDtoList = prescriptionService.getAllPrescriptions(userId);
        ApiResponse<List<PrescriptionResponseDto>> apiResponse = ApiResponse.<List<PrescriptionResponseDto>>builder().code(HttpStatus.OK.value()).data(prescriptionResponseDtoList).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }
    @PreAuthorize("hasAnyRole({'ROLE_DOCTOR', 'ROLE_USER'})")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PrescriptionResponseDto>> getPrescriptionById(@RequestParam(required = false, value = "user_id") Long userId,@PathVariable Long id) {
        PrescriptionResponseDto prescriptionResponseDto = prescriptionService.getPrescriptionById(id, userId);
        ApiResponse<PrescriptionResponseDto> apiResponse = ApiResponse.<PrescriptionResponseDto>builder().code(HttpStatus.OK.value()).data(prescriptionResponseDto).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }
//    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PrescriptionResponseDto>> createPrescription(@RequestBody CreatePrescriptionDto createPrescriptionDto) {
        PrescriptionResponseDto prescriptionResponseDto = prescriptionService.createPrescription(createPrescriptionDto);
        ApiResponse<PrescriptionResponseDto> apiResponse = ApiResponse.<PrescriptionResponseDto>builder().code(HttpStatus.OK.value()).data(prescriptionResponseDto).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePrescription(@RequestParam(value = "user_id") Long userId, @PathVariable Long id) {
        String result = prescriptionService.deletePrescription(userId, id);
        ApiResponse apiResponse = ApiResponse.builder().code(HttpStatus.OK.value()).message(result).build();
        return ResponseEntity.ok(apiResponse);
    }
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PrescriptionResponseDto>> updatePrescription(@PathVariable Long id, @RequestBody CreatePrescriptionDto createPrescriptionDto) {
        PrescriptionResponseDto prescriptionResponseDto = prescriptionService.updatePrescription(id, createPrescriptionDto);
        ApiResponse<PrescriptionResponseDto> apiResponse = ApiResponse.<PrescriptionResponseDto>builder().code(HttpStatus.OK.value()).data(prescriptionResponseDto).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }
}
