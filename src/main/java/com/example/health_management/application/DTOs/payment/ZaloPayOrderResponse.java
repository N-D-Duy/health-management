package com.example.health_management.application.DTOs.payment;
import lombok.Data;

import java.io.Serializable;

@Data
public class ZaloPayOrderResponse implements Serializable {
    private Integer returnCode;
    private String returnMessage;
    private Integer subReturnCode;
    private String subReturnMessage;
    private String zpTransToken;
    private String orderUrl;
    private String orderToken;
    private String qrCode;
    private String cashierOrderUrl;
}
