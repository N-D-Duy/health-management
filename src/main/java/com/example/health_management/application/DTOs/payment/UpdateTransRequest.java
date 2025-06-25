package com.example.health_management.application.DTOs.payment;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class UpdateTransRequest implements Serializable {
    private String zpTransToken;
    private Long appointmentId;
    private String transactionId;
}
