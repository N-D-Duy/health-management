package com.example.health_management.domain.cqrs.commands.impl.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class RefreshTokenCommand {
    private String accessToken;
    private String refreshToken;
}
