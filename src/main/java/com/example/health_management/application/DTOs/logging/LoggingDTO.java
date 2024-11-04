package com.example.health_management.application.DTOs.logging;

import com.example.health_management.common.shared.enums.LoggingType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoggingDTO {
    private String message;
    private LoggingType type;
}
