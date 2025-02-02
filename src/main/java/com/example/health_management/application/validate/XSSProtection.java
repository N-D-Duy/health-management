package com.example.health_management.application.validate;

import org.apache.commons.lang3.StringEscapeUtils;

public class XSSProtection {
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        String cleanedInput = input.replaceAll("[<>]", "");
        return StringEscapeUtils.escapeHtml4(cleanedInput);
    }
}

