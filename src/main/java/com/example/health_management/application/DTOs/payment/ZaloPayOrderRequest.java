package com.example.health_management.application.DTOs.payment;
import com.example.health_management.common.Constants;
import com.example.health_management.common.utils.zalopay.h_mac.ZaloPayHelper;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Builder
@Data
public class ZaloPayOrderRequest implements Serializable {
    private Integer appId;
    private String appUser;
    private Long amount;
    private String appTransId;
    private String bankCode;
    private Long appTime;
    private String embedData;
    private String item;
    private String mac;
    private String description;
    public void getMacWithHelper() throws NoSuchAlgorithmException, InvalidKeyException {
        String inputHMac = String.format("%s|%s|%s|%s|%s|%s|%s",
                this.appId,
                this.appTransId,
                this.appUser,
                this.amount,
                this.appTime,
                this.embedData,
                this.item);

        mac = ZaloPayHelper.getMac(Constants.MAC_KEY, inputHMac);
    }
}
