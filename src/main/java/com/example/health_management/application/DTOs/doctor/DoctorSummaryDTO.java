package com.example.health_management.application.DTOs.doctor;

import com.example.health_management.application.DTOs.user.response.UserSummaryDTO;
import com.example.health_management.common.shared.enums.DoctorSpecialization;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DoctorSummaryDTO extends UserSummaryDTO {
    private DoctorSpecialization specialization;
    private Double experience;
    private String qualification;
    private Double rating;
    private String about;
}
