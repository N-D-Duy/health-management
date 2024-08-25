package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.auth.RegisterDto;
import com.example.health_management.domain.cqrs.commands.handler.auth.RegisterByEmailCommandHandler;
import com.example.health_management.domain.cqrs.commands.impl.auth.RegisterByEmailCommand;
import com.example.health_management.domain.entities.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AuthService {

    public AuthService() {
    }

    public User register(RegisterDto registerDto) {
        return new RegisterByEmailCommandHandler().handle((new RegisterByEmailCommand(registerDto.getAccount())));
    }
}
