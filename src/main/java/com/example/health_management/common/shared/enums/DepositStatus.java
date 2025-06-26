package com.example.health_management.common.shared.enums;

public enum DepositStatus {
    NONE,
    HOLD,
    USED,
    FULL_REFUND_PENDING,
    PARTIAL_REFUND_PENDING,
    LOST,
    REFUNDED;

    public double getRefundRate() {
        return switch (this) {
//            case HOLD:
//            case USED:
            case FULL_REFUND_PENDING -> 1; // Full refund amount
            case PARTIAL_REFUND_PENDING -> 0.5; // Partial forfeiture amount
            default -> throw new IllegalArgumentException("Unknown deposit status: " + this);
        };
    }

}
