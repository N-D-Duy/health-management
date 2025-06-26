package com.example.health_management.application.DTOs.doctor;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DoctorAvailableResponse {
    LocalDateTime time;
    boolean isAvailable;
}
