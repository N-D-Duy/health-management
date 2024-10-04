//package com.example.health_management.api.controllers;
//
//
//import com.example.health_management.application.DTOs.medical_record.CreateMedicalRecordDto;
//import com.example.health_management.application.DTOs.medical_record.MedicalRecordResponseDto;
//import com.example.health_management.application.apiresponse.ApiResponse;
//import com.example.health_management.domain.services.MedicalRecordService;
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
//@RequestMapping("/medical-record")
//@Tag(name = "Medical Record", description = "Medical Record API")
//@SecurityRequirement(name = "bearerAuth")
//public class MedicalRecordController {
//    //todo: add userId as the criteria for fetching prescription as well
//
//    private MedicalRecordService medicalRecordService;
//
//    @Autowired
//    public MedicalRecordController(MedicalRecordService medicalRecordService) {
//        this.medicalRecordService = medicalRecordService;
//    }
//    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_DOCTOR'})")
//    @GetMapping("/all")
//    public ResponseEntity<ApiResponse<List<MedicalRecordResponseDto>>> getAllMedicalRecords(@RequestParam(value = "userId", required = false) Long userId) {
//        List<MedicalRecordResponseDto> medicalRecordResponseDto = medicalRecordService.getAllMedicalRecords(userId);
//        ApiResponse<List<MedicalRecordResponseDto>> apiResponse = ApiResponse.<List<MedicalRecordResponseDto>>builder().code(HttpStatus.OK.value()).data(medicalRecordResponseDto).message("Success").build();
//        return ResponseEntity.ok(apiResponse);
//    }
//
//    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_DOCTOR'})")
//    @GetMapping()
//    public ResponseEntity<ApiResponse<MedicalRecordResponseDto>> getMedicalRecordById(@RequestParam(value = "appointmentId") Long appointmentId) {
//        MedicalRecordResponseDto medicalRecordResponseDto = medicalRecordService.getMedicalRecordByAppointmentId(appointmentId);
//        ApiResponse<MedicalRecordResponseDto> apiResponse = ApiResponse.<MedicalRecordResponseDto>builder().code(HttpStatus.OK.value()).data(medicalRecordResponseDto).message("Success").build();
//        return ResponseEntity.ok(apiResponse);
//    }
//    @PreAuthorize("hasRole('ROLE_DOCTOR')")
//    @PostMapping("/create")
//    public ResponseEntity<ApiResponse<MedicalRecordResponseDto>> createMedicalRecord(@RequestBody CreateMedicalRecordDto medicalRecordRequestDto) {
//        MedicalRecordResponseDto medicalRecordResponseDto = medicalRecordService.createMedicalRecord(medicalRecordRequestDto);
//        ApiResponse<MedicalRecordResponseDto> apiResponse = ApiResponse.<MedicalRecordResponseDto>builder().code(HttpStatus.OK.value()).data(medicalRecordResponseDto).message("Success").build();
//        return ResponseEntity.ok(apiResponse);
//    }
//
//    @PreAuthorize("hasRole('ROLE_DOCTOR')")
//    @PutMapping("/update")
//    public ResponseEntity<ApiResponse<MedicalRecordResponseDto>> updateMedicalRecord(@RequestBody CreateMedicalRecordDto medicalRecordRequestDto) {
//        MedicalRecordResponseDto medicalRecordResponseDto = medicalRecordService.updateMedicalRecord(medicalRecordRequestDto);
//        ApiResponse<MedicalRecordResponseDto> apiResponse = ApiResponse.<MedicalRecordResponseDto>builder().code(HttpStatus.OK.value()).data(medicalRecordResponseDto).message("Success").build();
//        return ResponseEntity.ok(apiResponse);
//    }
//    @PreAuthorize("hasRole('ROLE_DOCTOR')")
//    @DeleteMapping("/delete")
//    public ResponseEntity<ApiResponse<String>> deleteMedicalRecord(@PathVariable(value = "appointmentId") Long appointmentId) {
//        String result = medicalRecordService.deleteMedicalRecord(appointmentId);
//        ApiResponse<String> apiResponse = ApiResponse.<String>builder().code(HttpStatus.OK.value()).data(result).message("Success").build();
//        return ResponseEntity.ok(apiResponse);
//    }
//}
