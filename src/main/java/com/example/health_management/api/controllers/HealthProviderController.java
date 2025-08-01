package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.heath_provider.HealthProviderDTO;
import com.example.health_management.application.DTOs.heath_provider.HealthProviderWithDoctorsDTO;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.domain.services.HealthProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/health-providers")
@RequiredArgsConstructor
public class HealthProviderController {
    private final HealthProviderService healthProviderService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<HealthProviderWithDoctorsDTO>>> getAllHealthProviders() {
        List<HealthProviderWithDoctorsDTO> healthProviderDTO = healthProviderService.findAll();
        ApiResponse<List<HealthProviderWithDoctorsDTO>> apiResponse = ApiResponse.<List<HealthProviderWithDoctorsDTO>>builder().code(HttpStatus.OK.value()).data(healthProviderDTO).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<HealthProviderDTO>> createHealthProvider(@RequestBody HealthProviderDTO healthProviderDTO) {
        HealthProviderDTO healthProvider = healthProviderService.create(healthProviderDTO);
        ApiResponse<HealthProviderDTO> apiResponse = ApiResponse.<HealthProviderDTO>builder().code(HttpStatus.OK.value()).data(healthProvider).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<HealthProviderWithDoctorsDTO>> updateHealthProvider(@RequestBody HealthProviderDTO healthProviderDTO) {
        HealthProviderWithDoctorsDTO healthProvider = healthProviderService.update(healthProviderDTO);
        ApiResponse<HealthProviderWithDoctorsDTO> apiResponse = ApiResponse.<HealthProviderWithDoctorsDTO>builder().code(HttpStatus.OK.value()).data(healthProvider).message("Success").build();
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{providerId}/doctors/{doctorId}")
    public ResponseEntity<ApiResponse<HealthProviderWithDoctorsDTO>> addDoctor(@PathVariable Long providerId,
                                                                               @PathVariable Long doctorId) {
        HealthProviderWithDoctorsDTO healthProvider = healthProviderService.doctorJoinHealthProvider(doctorId, providerId);
        ApiResponse<HealthProviderWithDoctorsDTO> apiResponse = ApiResponse.<HealthProviderWithDoctorsDTO>builder()
                .code(HttpStatus.OK.value())
                .data(healthProvider)
                .message("Success")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{providerId}/doctors/{doctorId}")
    public ResponseEntity<ApiResponse<HealthProviderWithDoctorsDTO>> doctorLeaveHealthProvider(@PathVariable Long providerId,
                                                                                               @PathVariable Long doctorId) {
        HealthProviderWithDoctorsDTO healthProvider = healthProviderService.doctorLeaveHealthProvider(doctorId, providerId);
        ApiResponse<HealthProviderWithDoctorsDTO> apiResponse = ApiResponse.<HealthProviderWithDoctorsDTO>builder()
                .code(HttpStatus.OK.value())
                .data(healthProvider)
                .message("Success")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/filter")
    public @ResponseBody ResponseEntity<ApiResponse<List<HealthProviderWithDoctorsDTO>>> filterHealthProviders(@Param("specialization") String specialization, @Param("startTime") LocalDateTime startTime) {
        List<HealthProviderWithDoctorsDTO> healthProviders = healthProviderService.getDoctorsAvailableForTimes(specialization, startTime);
        ApiResponse<List<HealthProviderWithDoctorsDTO>> apiResponse = ApiResponse.<List<HealthProviderWithDoctorsDTO>>builder()
                .code(HttpStatus.OK.value())
                .data(healthProviders)
                .message("Success")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
