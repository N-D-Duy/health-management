package com.example.health_management.domain.cqrs.commands.handler.auth;

import com.example.health_management.application.DTOs.auth.AuthResponseDto;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.domain.cqrs.commands.impl.auth.AuthCommand;
import com.example.health_management.domain.entities.Account;
import com.example.health_management.domain.entities.Key;
import com.example.health_management.domain.entities.Payload;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AccountRepository;
import com.example.health_management.domain.repositories.KeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthCommandHandler {
    private final KeyRepository keyRepository;
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AuthResponseDto handle(AuthCommand command) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                command.getEmail(),
                command.getPassword()
        ));

        final Account account = accountRepository.findByEmail(command.getEmail());
        final User user = account.getUser();

        final Key key = keyRepository.findKeyByUserId(user.getId());
        final String privateKey = key.getPrivateKey();
        final Payload payload = new Payload(user.getId(), account.getRole().name(), key.getVersion(), account.getEmail());
        final String accessToken = jwtProvider.generateAccessToken(payload, privateKey);
        final String refreshToken = jwtProvider.generateRefreshToken(payload, privateKey);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
