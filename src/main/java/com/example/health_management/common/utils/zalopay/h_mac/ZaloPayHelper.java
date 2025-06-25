package com.example.health_management.common.utils.zalopay.h_mac;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ZaloPayHelper {
    private static int transIdDefault = 1;

    @NotNull
    public static String getAppTransId() {
        if (transIdDefault >= 100000) {
            transIdDefault = 1;
        }

        transIdDefault += 1;
        SimpleDateFormat formatDateTime = new SimpleDateFormat("yyMMdd_hhmmss");
        String timeString = formatDateTime.format(new Date());
        return String.format("%s%06d", timeString, transIdDefault);
    }

    @NotNull
    public static String getMac(@NotNull String key, @NotNull String data) throws NoSuchAlgorithmException, InvalidKeyException {
        return Objects.requireNonNull(HMacUtil.HMacHexStringEncode(HMacUtil.HMACSHA256, key, data));
    }

@NotNull
public static String getMRefundId(@NotNull int appId) {
    Random rand = new Random();
    long timestamp = System.currentTimeMillis(); // miliseconds
    String uid = timestamp + "" + (111 + rand.nextInt(888)); // unique id
    String datePart = getCurrentTimeString("yyMMdd");
    return String.format("%s_%d_%s", datePart, appId, uid);
}

    private static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }


}
