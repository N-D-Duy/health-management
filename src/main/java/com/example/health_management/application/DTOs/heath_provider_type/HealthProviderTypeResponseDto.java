package com.example.health_management.application.DTOs.heath_provider_type;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.health_management.domain.entities.HealthProviderType}
 */
@Value
public class HealthProviderTypeResponseDto implements Serializable {
    int id;
    String typeName;
    String typeDescription;
}