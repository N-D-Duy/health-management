package com.example.health_management.domain.cqrs.commands.handler.auth;

import com.example.health_management.application.guards.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutCommandHandler {
    private final JwtProvider jwtProvider;


    public void handle() {
        try{
            jwtProvider.endSession();
        } catch (Exception e) {
            throw new RuntimeException("Error while logging out (update key failed)");
        }
    }

}
