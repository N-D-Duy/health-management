//package com.example.health_management.api.controllers;
//
//import com.example.health_management.application.DTOs.prescription.PrescriptionDTO;
//import com.example.health_management.application.apiresponse.ApiResponse;
//import com.example.health_management.domain.services.PrescriptionService;
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
//@RequestMapping("/prescription")
//@SecurityRequirement(name = "bearerAuth")
//@Tag(name = "Prescription", description = "Prescription API")
//public class PrescriptionController {
//
//    private final PrescriptionService prescriptionService;
//    @Autowired
//    public PrescriptionController(PrescriptionService prescriptionService) {
//        this.prescriptionService = prescriptionService;
//    }
//
//    @PostMapping("/create")
//    @PreAuthorize("hasRole('DOCTOR')")
//    public ResponseEntity<ApiResponse<PrescriptionDTO>> create(@RequestBody PrescriptionRequest request) {
//        PrescriptionDTO prescriptionDTO = prescriptionService.create(request);
//        ApiResponse<PrescriptionDTO> apiResponse = ApiResponse.<PrescriptionDTO>builder().code(HttpStatus.OK.value()).data(prescriptionDTO).message("Success").build();
//        return ResponseEntity.ok(apiResponse);
//    }
//
//    @GetMapping("/all")
//    @PreAuthorize("hasRole('DOCTOR')")
//    public ResponseEntity<ApiResponse<List<PrescriptionDTO>>> getAllPrescriptions() {
//        List<PrescriptionDTO> prescriptionDTO = prescriptionService.getAllPrescriptions();
//        ApiResponse<List<PrescriptionDTO>> apiResponse = ApiResponse.<List<PrescriptionDTO>>builder().code(HttpStatus.OK.value()).data(prescriptionDTO).message("Success").build();
//        return ResponseEntity.ok(apiResponse);
//    }
//}
