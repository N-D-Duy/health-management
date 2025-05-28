package com.example.health_management.application.DTOs.payment;

import lombok.Data;

import java.io.Serializable;
@Data
public class MerchantAppCreateOrderRequest implements Serializable {
    private Long amount;
    private Long userId;
    private String description;
}
