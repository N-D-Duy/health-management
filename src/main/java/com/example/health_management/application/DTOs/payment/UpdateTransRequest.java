package com.example.health_management.application.DTOs.payment;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateTransRequest {
    private String zpTokens;
    private Long appointmentId;
    private String transactionId;
}
