package com.example.health_management.application.DTOs.heath_provider;

import com.example.health_management.application.DTOs.address.AddressDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.example.health_management.domain.entities.HealthProvider}
 */
@Data
public class HealthProviderDTO implements Serializable {
    private Long id;
    private String name;
    private AddressDTO address;
    private String description;
}