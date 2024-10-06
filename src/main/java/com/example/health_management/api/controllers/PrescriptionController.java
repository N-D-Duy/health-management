package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.prescription.CreatePrescriptionRequest;
import com.example.health_management.application.DTOs.prescription.PrescriptionDTO;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.domain.services.PrescriptionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<PrescriptionDTO>>> getAllPrescriptions(@RequestParam(required = false, value = "user_id") Long userId) {
        List<PrescriptionDTO> prescriptionDTOList = prescriptionService.getAllPrescriptions(userId);
        ApiResponse<List<PrescriptionDTO>> apiResponse = ApiResponse.<List<PrescriptionDTO>>builder().code(HttpStatus.OK.value()).data(prescriptionDTOList).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }
    @PreAuthorize("hasAnyRole({'ROLE_DOCTOR', 'ROLE_USER'})")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PrescriptionDTO>> getPrescriptionById(@RequestParam(required = false, value = "user_id") Long userId, @PathVariable Long id) {
        PrescriptionDTO prescriptionDTO = prescriptionService.getPrescriptionById(id, userId);
        ApiResponse<PrescriptionDTO> apiResponse = ApiResponse.<PrescriptionDTO>builder().code(HttpStatus.OK.value()).data(prescriptionDTO).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }
//    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PrescriptionDTO>> createPrescription(@RequestBody CreatePrescriptionRequest createPrescriptionRequest) {
        PrescriptionDTO prescriptionDTO = prescriptionService.createPrescription(createPrescriptionRequest);
        ApiResponse<PrescriptionDTO> apiResponse = ApiResponse.<PrescriptionDTO>builder().code(HttpStatus.OK.value()).data(prescriptionDTO).message("Success").build();
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
    public ResponseEntity<ApiResponse<PrescriptionDTO>> updatePrescription(@PathVariable Long id, @RequestBody CreatePrescriptionRequest createPrescriptionRequest) {
        PrescriptionDTO prescriptionDTO = prescriptionService.updatePrescription(id, createPrescriptionRequest);
        ApiResponse<PrescriptionDTO> apiResponse = ApiResponse.<PrescriptionDTO>builder().code(HttpStatus.OK.value()).data(prescriptionDTO).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }
}
