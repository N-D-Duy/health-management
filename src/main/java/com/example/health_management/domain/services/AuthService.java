package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.auth.AuthRequestDto;
import com.example.health_management.application.DTOs.auth.AuthResponseDto;
import com.example.health_management.application.DTOs.auth.RegisterDto;
import com.example.health_management.domain.cqrs.commands.handler.auth.AuthCommandHandler;
import com.example.health_management.domain.cqrs.commands.handler.auth.LogoutCommandHandler;
import com.example.health_management.domain.cqrs.commands.handler.auth.RefreshTokenCommandHandler;
import com.example.health_management.domain.cqrs.commands.handler.auth.RegisterCommandHandler;
import com.example.health_management.domain.cqrs.commands.impl.auth.AuthCommand;
import com.example.health_management.domain.cqrs.commands.impl.auth.RefreshTokenCommand;
import com.example.health_management.domain.cqrs.commands.impl.auth.RegisterCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthCommandHandler authCommandHandler;
    private final RegisterCommandHandler registerCommandHandler;
    private final RefreshTokenCommandHandler refreshTokenHandler;
    private final LogoutCommandHandler logoutCommandHandler;

    public AuthResponseDto register(RegisterDto registerDto) {
        return registerCommandHandler.handle((new RegisterCommand(
                registerDto.getEmail(),
                registerDto.getUsername(),
                registerDto.getPassword(),
                registerDto.getRole(),
                registerDto.getNotificationKey()
        )));
    }

    public AuthResponseDto authenticate(AuthRequestDto authRequestDto) {
        return authCommandHandler.handle(new AuthCommand(authRequestDto.getEmail(), authRequestDto.getPassword()));
    }

    public AuthResponseDto refreshToken(String refreshToken) {
        return refreshTokenHandler.handle(new RefreshTokenCommand(refreshToken));
    }

    public void logout(String uid) {
        logoutCommandHandler.handle(uid);
    }

}
