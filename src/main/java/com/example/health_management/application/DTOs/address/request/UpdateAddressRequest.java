package com.example.health_management.application.DTOs.address.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.example.health_management.domain.entities.Address}
 */
@Builder
@Data
public class UpdateAddressRequest implements Serializable {
    private Long id;
    private String unitNumber;
    private String streetNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String region;
    private String postalCode;
    private String country;
    private byte isDefault;
}