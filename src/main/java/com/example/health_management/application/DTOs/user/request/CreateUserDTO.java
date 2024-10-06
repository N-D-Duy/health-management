package com.example.health_management.application.DTOs.user.request;

import lombok.Data;

@Data
public class CreateUserDTO {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
}
