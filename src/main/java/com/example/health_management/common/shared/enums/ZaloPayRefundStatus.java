package com.example.health_management.common.shared.enums;

public enum ZaloPayRefundStatus {
    SUCCESS(1, "Giao dịch thành công"),
    FAILED(2, "Giao dịch thất bại"),
    PENDING(3, "Giao dịch đang chờ xử lý");

    private final int code;
    private final String description;

    ZaloPayRefundStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
    public static ZaloPayRefundStatus fromCode(int code) {
        for (ZaloPayRefundStatus status : ZaloPayRefundStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
