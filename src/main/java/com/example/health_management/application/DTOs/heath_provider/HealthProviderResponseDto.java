package com.example.health_management.application.DTOs.heath_provider;

import com.example.health_management.application.DTOs.heath_provider_type.HealthProviderTypeResponseDto;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.health_management.domain.entities.HealthProvider}
 */
@Value
public class HealthProviderResponseDto implements Serializable {
    int id;
    String healthProviderName;
    HealthProviderTypeResponseDto healthProviderType;
}