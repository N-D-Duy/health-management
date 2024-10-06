package com.example.health_management.application.DTOs.account;

import lombok.Data;

@Data
public class UpdateAccountRequest {
    private String username;
    private String email;
    private String password;
    private String phone;
}
