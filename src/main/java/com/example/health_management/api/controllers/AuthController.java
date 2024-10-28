package com.example.health_management.api.controllers;


import com.example.health_management.application.DTOs.auth.AuthResponse;
import com.example.health_management.application.DTOs.auth.RegisterDTO;
import com.example.health_management.domain.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public @ResponseBody AuthResponse register(@RequestBody RegisterDTO registerDto) {
        return authService.register(registerDto);
    }

    @PostMapping("/login")
    public void login(HttpServletResponse response) {
    }

    @PostMapping("/refresh-token")
    public @ResponseBody AuthResponse refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        return authService.refreshToken(refreshToken);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        authService.logout(refreshToken);
    }


}
