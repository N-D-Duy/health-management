package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.auth.AuthRequest;
import com.example.health_management.application.DTOs.auth.AuthResponse;
import com.example.health_management.application.DTOs.auth.RegisterDTO;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.cqrs.commands.handler.auth.AuthCommandHandler;
import com.example.health_management.domain.cqrs.commands.handler.auth.LogoutCommandHandler;
import com.example.health_management.domain.cqrs.commands.handler.auth.RefreshTokenCommandHandler;
import com.example.health_management.domain.cqrs.commands.handler.auth.RegisterCommandHandler;
import com.example.health_management.domain.cqrs.commands.impl.auth.AuthCommand;
import com.example.health_management.domain.cqrs.commands.impl.auth.RefreshTokenCommand;
import com.example.health_management.domain.cqrs.commands.impl.auth.RegisterCommand;
import com.example.health_management.domain.repositories.AccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {
    private final AuthCommandHandler authCommandHandler;
    private final RegisterCommandHandler registerCommandHandler;
    private final RefreshTokenCommandHandler refreshTokenHandler;
    private final LogoutCommandHandler logoutCommandHandler;
    private final AccountRepository accountRepository;

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

    public boolean active(String email) {
        try{
            return accountRepository.activeAccount(email) == 1;
        } catch (Exception ex){
            throw  new ConflictException(ex.getMessage());
        }
    }

}
