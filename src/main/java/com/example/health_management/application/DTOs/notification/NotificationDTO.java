package com.example.health_management.application.DTOs.notification;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long userId;
    private String title;
    private String content;
}
