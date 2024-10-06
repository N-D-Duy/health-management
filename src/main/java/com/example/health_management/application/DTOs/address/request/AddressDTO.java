package com.example.health_management.application.DTOs.address.request;

import com.example.health_management.application.DTOs.country.CountryDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.health_management.domain.entities.Address}
 */
@Value
@Builder
@Getter @Setter
public class AddressDTO implements Serializable {
    String unitNumber;
    String streetNumber;
    String addressLine1;
    String addressLine2;
    String city;
    String region;
    String postalCode;
    CountryDTO country;
}