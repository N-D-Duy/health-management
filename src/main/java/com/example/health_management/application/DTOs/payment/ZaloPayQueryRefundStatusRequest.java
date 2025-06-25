package com.example.health_management.application.DTOs.payment;

import com.example.health_management.common.Constants;
import com.example.health_management.common.utils.zalopay.h_mac.ZaloPayHelper;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
public class ZaloPayQueryRefundStatusRequest implements Serializable {
    private int appId;
    @JsonProperty("m_refund_id")
    private String mRefundId;
    private Long timestamp;
    private String mac;

    public void setMacWithHelper() throws Exception {
        String inputHMac = String.format("%s|%s|%s",
                this.appId,
                this.mRefundId,
                this.timestamp);

        this.mac = ZaloPayHelper.getMac(Constants.MAC_KEY, inputHMac);
    }
}
