package com.example.health_management.common.shared.enums;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum Permission {
    ALL_READ("all:read"),
    ALL_UPDATE("all:update"),
    ALL_CREATE("all:create"),
    ALL_DELETE("all:delete"),

    // Manager permissions for doctor management
    DOCTOR_READ("doctor:read"),
    DOCTOR_UPDATE("doctor:update"),
    DOCTOR_CREATE("doctor:create"),
    DOCTOR_DELETE("doctor:delete"),

    // Basic read permission
    BASIC_READ("basic:read");
    private final String permission;
}

