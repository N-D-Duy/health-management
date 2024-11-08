package com.example.health_management.application.DTOs.heath_provider;

import com.example.health_management.application.DTOs.user.response.DoctorSummaryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class HealthProviderWithDoctorsDTO extends HealthProviderDTO{
    private List<DoctorSummaryDTO> doctors;
}
