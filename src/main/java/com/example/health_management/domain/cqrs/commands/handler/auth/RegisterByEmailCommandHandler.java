package com.example.health_management.domain.cqrs.commands.handler.auth;

import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.cqrs.commands.impl.auth.RegisterByEmailCommand;
import com.example.health_management.domain.entities.Account;
import com.example.health_management.domain.entities.Key;
import com.example.health_management.domain.entities.Payload;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AccountRepository;
import com.example.health_management.domain.repositories.KeyRepository;
import com.example.health_management.domain.repositories.UserRepository;
import com.example.health_management.domain.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@Component
public class RegisterByEmailCommandHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private KeyRepository keyRepository;

    @Autowired
    private JwtService jwtService;


    public User handle(RegisterByEmailCommand command) {
        // Check if email already exists
        Account existingAccount = accountRepository.findByEmail(command.getAccount().getEmail());
        if (existingAccount != null) {
            throw new ConflictException("error.emailAlreadyExists");
        }

        // Create Account entity
        Account account = new Account();
        account.setUsername(command.getAccount().getUsername());
        account.setPassword(command.getAccount().getPassword());

        // Create User entity
        User user = new User();
        user.setAccount(account);
        userRepository.save(user);

        // Create Key entity
        Key key = new Key();
        Payload payload = new Payload();
        payload.setId(user.getId());
        payload.setEmail(account.getEmail());
        payload.setRole(account.getRole().toString());

        Map<String, String> keyPair = jwtService.generateKeyPair();
        key.setPublicKey(keyPair.get("publicKey"));
        key.setPrivateKey(keyPair.get("privateKey"));
        key.setRefreshToken(jwtService.generateRefreshToken(payload, keyPair.get("privateKey")));

        key.setUser(user);
        user.setKey(key);

        return user;
    }
}

