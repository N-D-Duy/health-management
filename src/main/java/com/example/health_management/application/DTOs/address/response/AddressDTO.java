package com.example.health_management.application.DTOs.address.response;

import com.example.health_management.application.DTOs.country.CountryDTO;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.health_management.domain.entities.Address}
 */
@Value
@Builder
public class AddressDTO implements Serializable {
    long id;
    String unitNumber;
    String streetNumber;
    String addressLine1;
    String addressLine2;
    String city;
    String region;
    String postalCode;
    CountryDTO country;
}