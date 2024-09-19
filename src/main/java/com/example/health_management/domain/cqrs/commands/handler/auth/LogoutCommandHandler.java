package com.example.health_management.domain.cqrs.commands.handler.auth;

import com.example.health_management.domain.repositories.KeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutCommandHandler {
    private final KeyRepository keyRepository;

    public void handle(String uid) {
        try{
            keyRepository.signOut(uid);
        } catch (Exception e) {
            throw new RuntimeException("Error while logging out (update key failed)");
        }
    }

}
