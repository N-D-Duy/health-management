package com.example.health_management.domain.cqrs.commands.handler.auth;

import java.util.Map;

import com.example.health_management.domain.repositories.KeyRepository;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.example.health_management.application.DTOs.auth.AuthResponse;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.domain.cqrs.commands.impl.auth.RefreshTokenCommand;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenCommandHandler {
    private final JwtProvider jwtProvider;
    private final KeyRepository keyRepository;

    public AuthResponse handle(RefreshTokenCommand command) {
        Map<String, String> tokens = jwtProvider.refreshToken(command.getRefreshToken());
        if (tokens == null) {
            return null;
        }
        return AuthResponse.builder()
                .accessToken(tokens.get("accessToken"))
                .refreshToken(tokens.get("refreshToken"))
                .build();
    }
}
