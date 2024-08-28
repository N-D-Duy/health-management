package com.example.health_management.application.DTOs.auth;

import com.example.health_management.common.shared.enums.Role;
import com.example.health_management.domain.entities.Account;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RegisterDto {
    private String email;
    private String username;
    private String password;
    private Role role;
}