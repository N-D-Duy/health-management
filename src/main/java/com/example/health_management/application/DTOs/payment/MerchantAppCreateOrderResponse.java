package com.example.health_management.application.DTOs.payment;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Data
@Builder
public class MerchantAppCreateOrderResponse implements Serializable {
    private String zpTransToken;
}
