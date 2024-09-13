package com.example.health_management.api.controllers;

import com.example.health_management.domain.services.AzureOpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diagnose-diseases")
public class AzureOpenAIController {
    private final AzureOpenAiService azureOpenAiService;

    @Autowired
    public AzureOpenAIController(AzureOpenAiService azureOpenAiService){
        this.azureOpenAiService = azureOpenAiService;
    }
    @GetMapping
    public ResponseEntity<String> jobReasons(@RequestParam(value = "heartRate") int heartRate,
                                             @RequestParam("bloodPressure") String bloodPressure,
                                             @RequestParam("bodyTemperature") int bodyTemperature,
                                             @RequestParam("respiratoryRate") int respiratoryRate,
                                             @RequestParam("sp02") int sp02
    ) {
        String result = azureOpenAiService.diagnoseDiseases(heartRate, bloodPressure, bodyTemperature, respiratoryRate, sp02);
        return ResponseEntity.ok().body(result);
    }
}
