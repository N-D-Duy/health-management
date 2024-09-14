package com.example.health_management.api.controllers;

import com.example.health_management.domain.services.AzureOpenAiService;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diagnose-diseases")
@JsonNaming()
@Tag(name = "AI Analysis Controller", description = "Endpoints for health analysis by AI")
public class AzureOpenAIController {
    private final AzureOpenAiService azureOpenAiService;

    @Autowired
    public AzureOpenAIController(AzureOpenAiService azureOpenAiService){
        this.azureOpenAiService = azureOpenAiService;
    }
    @GetMapping
    public ResponseEntity<String> jobReasons(@RequestParam("heart_rate") int heartRate,
                                             @RequestParam("blood_pressure") String bloodPressure,
                                             @RequestParam("body_temperature") int bodyTemperature,
                                             @RequestParam("respiratory_rate") int respiratoryRate,
                                             @RequestParam("sp_o2") int spO2
    ) {
        String result = azureOpenAiService.diagnoseDiseases(heartRate, bloodPressure, bodyTemperature, respiratoryRate, spO2);
        return ResponseEntity.ok().body(result);
    }
}
