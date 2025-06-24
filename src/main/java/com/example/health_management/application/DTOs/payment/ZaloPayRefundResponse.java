package com.example.health_management.application.DTOs.payment;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ZaloPayRefundResponse  implements Serializable {
    private final int returnCode;
    private final String returnMessage;
    private final int subReturnCode;
    private final String subReturnMessage;
}
