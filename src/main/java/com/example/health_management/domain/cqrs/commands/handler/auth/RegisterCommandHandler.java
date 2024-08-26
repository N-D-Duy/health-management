package com.example.health_management.domain.cqrs.commands.handler.auth;

import com.example.health_management.application.guards.JwtProvider;
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
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RegisterCommandHandler {

    private final UserRepository userRepository;


    private final AccountRepository accountRepository;

    private final KeyRepository keyRepository;


    private final JwtProvider jwtProvider;


    public User handle(RegisterCommand command) {
        // Check if email already exists
        // command is a DTO object that contains the account details
        Account existingAccount = accountRepository.findByEmail(command.getEmail());
        if (existingAccount != null) {
            throw new ConflictException("error.emailAlreadyExists");
        }

        // Create User entity
        User user = new User();

        // Create Account entity
        Account account = new Account();
        account.setUsername(command.getUsername());
        account.setPassword(command.getPassword());
        account.setEmail(command.getEmail());
        account.setRole(command.getRole());
        account.setUser(user);  // Set the user for account

        // Associate account with user
        user.setAccount(account);

        // Save User (which will also save Account due to CascadeType.ALL)
        userRepository.save(user); //error here

        // Create Key entity
        Key key = new Key();
        Payload payload = new Payload();
        payload.setId(user.getId());
        payload.setEmail(account.getEmail());
        payload.setRole(account.getRole().toString());

        Map<String, String> keyPair = jwtProvider.generateKeyPair();
        key.setPublicKey(keyPair.get("publicKey"));
        key.setPrivateKey(keyPair.get("privateKey"));
        key.setRefreshToken(jwtProvider.generateRefreshToken(payload, keyPair.get("privateKey")));

        key.setUser(user);  // Set the user for key
        user.setKey(key);   // Associate key with user

        // Save Key (you may need to do this depending on your Cascade settings)
        keyRepository.save(key);

        return user;
    }
}

