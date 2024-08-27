package com.example.health_management.domain.cqrs.commands.impl.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthCommand {
    private String email;
    private String password;
}
