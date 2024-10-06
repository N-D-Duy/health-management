package com.example.health_management.application.DTOs.user.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String avatarUrl;
}
