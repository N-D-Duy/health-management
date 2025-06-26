package com.example.health_management.application.DTOs.payment;

import lombok.Data;

import java.io.Serializable;
@Data
public class ZaloPayQueryRefundStatusResponse implements Serializable {
    private int returnCode;
    private String returnMessage;
    private int subReturnCode;
    private String subReturnMessage;
}
