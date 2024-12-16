package com.example.health_management.api.controllers;

import com.example.health_management.application.DTOs.analysis.DataAnalysis;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.domain.services.DataAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class DataAnalysisController {
    private final DataAnalysisService dataAnalysisService;

    @GetMapping
    public ResponseEntity<ApiResponse<DataAnalysis>> getDataAnalysis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        DataAnalysis data = dataAnalysisService.getDataAnalysis(startDate, endDate);
        ApiResponse<DataAnalysis> response = ApiResponse.<DataAnalysis>builder()
                .code(200)
                .data(data)
                .message("Data analysis retrieved successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
