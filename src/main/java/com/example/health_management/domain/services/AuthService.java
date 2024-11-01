package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.auth.AuthRequest;
import com.example.health_management.application.DTOs.auth.AuthResponse;
import com.example.health_management.application.DTOs.auth.RegisterDTO;
import com.example.health_management.domain.cqrs.commands.handler.auth.AuthCommandHandler;
import com.example.health_management.domain.cqrs.commands.handler.auth.LogoutCommandHandler;
import com.example.health_management.domain.cqrs.commands.handler.auth.RefreshTokenCommandHandler;
import com.example.health_management.domain.cqrs.commands.handler.auth.RegisterCommandHandler;
import com.example.health_management.domain.cqrs.commands.impl.auth.AuthCommand;
import com.example.health_management.domain.cqrs.commands.impl.auth.RefreshTokenCommand;
import com.example.health_management.domain.cqrs.commands.impl.auth.RegisterCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {
    private final AuthCommandHandler authCommandHandler;
    private final RegisterCommandHandler registerCommandHandler;
    private final RefreshTokenCommandHandler refreshTokenHandler;
    private final LogoutCommandHandler logoutCommandHandler;

    public AuthResponse register(RegisterDTO registerDto) {
        try {
            return registerCommandHandler.handle((new RegisterCommand(
                    registerDto.getEmail(),
                    registerDto.getUsername(),
                    registerDto.getPassword(),
                    registerDto.getRole()
            )));
        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }

    public AuthResponse authenticate(@NonNull AuthRequest authRequest) {
        return authCommandHandler.handle(new AuthCommand(authRequest.getEmail(), authRequest.getPassword()));
    }

    public AuthResponse refreshToken(String refreshToken) {
        return refreshTokenHandler.handle(new RefreshTokenCommand(refreshToken));
    }

    public String logout() {
        return logoutCommandHandler.handle();
    }

}
