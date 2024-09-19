package com.example.health_management.domain.cqrs.commands.handler.auth;

import com.example.health_management.application.DTOs.auth.AuthResponseDto;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.common.shared.enums.Role;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.cqrs.commands.impl.auth.RegisterCommand;
import com.example.health_management.domain.entities.Account;
import com.example.health_management.domain.entities.Key;
import com.example.health_management.domain.entities.Payload;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AccountRepository;
import com.example.health_management.domain.repositories.KeyRepository;
import com.example.health_management.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RegisterCommandHandler {

    private final UserRepository userRepository;


    private final AccountRepository accountRepository;

    private final KeyRepository keyRepository;


    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;


    public AuthResponseDto handle(RegisterCommand command) {
        // Check if email already exists
        // command is a DTO object that contains the account details
        final Logger logger = LoggerFactory.getLogger(RegisterCommandHandler.class);

        logger.warn("checking email");
        Account existingAccount = accountRepository.findByEmail(command.getEmail());
        if (existingAccount != null) {
            throw new ConflictException("error.emailAlreadyExists");
        }

        // Create User entity
        User user = new User();
        userRepository.save(user);

        if(command.getRole() == null) {
            command.setRole(Role.USER);
        }

        logger.warn("creating account");
        // Hash password
        command.setPassword(passwordEncoder.encode(command.getPassword()));
        // Create Account entity
        Account account = new Account();
        account.setEmail(command.getEmail());
        account.setUsername(command.getUsername());
        account.setRole(command.getRole());
        account.setPassword(command.getPassword());
        account.setUser(user);

        // Create Key entity
        Key key = new Key();
        Payload payload = new Payload();
        payload.setVersion(1);
        payload.setId(user.getId());
        payload.setEmail(account.getEmail());
        payload.setRole(account.getRole().toString());

        logger.warn("creating key pair");
        Map<String, String> keyPair = jwtProvider.generateKeyPair();
        key.setPublicKey(keyPair.get("publicKey"));
        key.setPrivateKey(keyPair.get("privateKey"));
        final String accessToken = jwtProvider.generateAccessToken(payload, keyPair.get("privateKey"));
        final String refreshToken = jwtProvider.generateRefreshToken(payload, keyPair.get("privateKey"));
        key.setRefreshToken(refreshToken);
        key.setVersion(1);
        key.setNotificationKey(command.getNotificationKey());

        logger.warn("key: {}", key);
        logger.warn("account: {}", account);
        logger.warn("user: {}", user);
        logger.warn("final set");
        // Associate account with user
        user.setAccount(account);
        key.setUser(user);  // Set the user for key
        user.setKey(key);   // Associate key with user

        // Save entities
        accountRepository.save(account);
        keyRepository.save(key);

        return AuthResponseDto.builder().accessToken(accessToken).refreshToken(key.getRefreshToken()).build();
    }
}

