package com.example.health_management.application.DTOs.user.response;

import lombok.Data;

@Data
public class UserSummaryDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String avatarUrl;
}