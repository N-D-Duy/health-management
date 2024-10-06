package com.example.health_management.application.DTOs.country;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.health_management.domain.entities.Country}
 */
@Value
public class CountryDTO implements Serializable {
    Long id;
    String name;
}