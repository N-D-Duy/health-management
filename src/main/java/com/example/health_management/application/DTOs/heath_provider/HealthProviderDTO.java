package com.example.health_management.application.DTOs.heath_provider;

import com.example.health_management.application.DTOs.address.response.AddressDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.example.health_management.domain.entities.HealthProvider}
 */
@Data
public class HealthProviderDTO implements Serializable {
    private int id;
    private String name;
    private List<AddressDTO> addresses;
}