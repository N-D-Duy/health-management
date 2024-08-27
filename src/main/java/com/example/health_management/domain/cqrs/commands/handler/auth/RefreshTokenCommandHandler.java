package com.example.health_management.domain.cqrs.commands.handler.auth;

import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.domain.cqrs.commands.impl.auth.RefreshTokenCommand;
import com.example.health_management.domain.repositories.AccountRepository;
import com.example.health_management.domain.repositories.KeyRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RefreshTokenCommandHandler {
    private final AccountRepository accountRepository;
    private final KeyRepository keyRepository;
    private final JwtProvider jwtProvider;

    public void handle(RefreshTokenCommand command) {
        String token = jwtProvider.extractToken(command.getRequest());
        if(token != null) {
            // extract the email from the token

            // get the public key from the database

        }
    }
}
