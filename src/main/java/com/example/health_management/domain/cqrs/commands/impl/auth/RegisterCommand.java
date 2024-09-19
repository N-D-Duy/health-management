package com.example.health_management.domain.cqrs.commands.impl.auth;

import com.example.health_management.common.shared.enums.Role;
import com.example.health_management.domain.entities.Account;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterCommand {
    private String email;
    private String username;
    private String password;
    private Role role;
    private String notificationKey;
}
