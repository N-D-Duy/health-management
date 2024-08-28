package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.auth.AuthRequestDto;
import com.example.health_management.application.DTOs.auth.AuthResponseDto;
import com.example.health_management.application.DTOs.auth.LoginDto;
import com.example.health_management.application.DTOs.auth.RegisterDto;
import com.example.health_management.domain.cqrs.commands.handler.auth.AuthCommandHandler;
import com.example.health_management.domain.cqrs.commands.handler.auth.RegisterCommandHandler;
import com.example.health_management.domain.cqrs.commands.impl.auth.AuthCommand;
import com.example.health_management.domain.cqrs.commands.impl.auth.RegisterCommand;
import com.example.health_management.domain.entities.Account;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AccountRepository;
import com.example.health_management.domain.repositories.KeyRepository;
import com.example.health_management.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthCommandHandler authCommandHandler;
    private final RegisterCommandHandler registerCommandHandler;

    public AuthResponseDto register(RegisterDto registerDto) {
        return registerCommandHandler.handle((new RegisterCommand(
                registerDto.getEmail(),
                registerDto.getUsername(),
                registerDto.getPassword(),
                registerDto.getRole()
        )));
    }

    public AuthResponseDto authenticate(AuthRequestDto authRequestDto) {
        return authCommandHandler.handle(new AuthCommand(authRequestDto.getEmail(), authRequestDto.getPassword()));
    }

    public AuthResponseDto login(LoginDto login) {
        return null;
    }

}
