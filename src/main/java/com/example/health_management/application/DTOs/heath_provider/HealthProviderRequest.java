package com.example.health_management.application.DTOs.heath_provider;

import com.example.health_management.application.DTOs.address.response.AddressDTO;
import lombok.Data;

@Data
public class HealthProviderRequest {
    private String name;
    private AddressDTO address;
    private String description;
}
