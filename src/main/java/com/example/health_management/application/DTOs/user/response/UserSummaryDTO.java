package com.example.health_management.application.DTOs.user.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserSummaryDTO implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String email;
    private String role;
}