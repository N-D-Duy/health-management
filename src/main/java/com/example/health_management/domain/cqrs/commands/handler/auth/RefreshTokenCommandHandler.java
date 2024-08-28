package com.example.health_management.domain.cqrs.commands.handler.auth;

import com.example.health_management.application.DTOs.auth.AuthResponseDto;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.domain.cqrs.commands.impl.auth.RefreshTokenCommand;
import com.example.health_management.domain.repositories.KeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RefreshTokenCommandHandler {
    private final JwtProvider jwtProvider;

    public AuthResponseDto handle(RefreshTokenCommand command) {
        Map<String, String> tokens = jwtProvider.refreshToken(command.getAccessToken(), command.getRefreshToken());
        return AuthResponseDto.builder()
                .accessToken(tokens.get("accessToken"))
                .refreshToken(tokens.get("refreshToken"))
                .build();
    }
}
