package com.example.health_management.application.DTOs.auth;

import com.example.health_management.common.shared.enums.Role;
import lombok.*;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RegisterDTO {
    private String email;
    private String username;
    private String password;
    private Role role;
}
