package com.example.health_management.domain.cqrs.commands.handler.auth;

import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.common.shared.exceptions.ConflictException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutCommandHandler {
    private final JwtProvider jwtProvider;


    public String handle() {
        try{
            return jwtProvider.endSession();
        } catch (Exception e) {
            throw new ConflictException("Error while logging out (update key failed)");
        }
    }

}
