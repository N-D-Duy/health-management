package com.example.health_management.application.DTOs.user.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DoctorDTO extends UserDTO {
    private Long id;
    private String specialization;
    private Double experience;
    private String qualification;
    private Double rating;
    private String about;
}
