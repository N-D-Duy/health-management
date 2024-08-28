package com.example.health_management.api.controllers;


import com.example.health_management.application.DTOs.auth.AuthResponseDto;
import com.example.health_management.application.DTOs.auth.TokensRequestDto;
import com.example.health_management.application.DTOs.auth.RegisterDto;
import com.example.health_management.domain.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public @ResponseBody AuthResponseDto register(@RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/health")
    public @ResponseBody String health() {
        return "OK";
    }

    @PostMapping("/login")
    public void login(HttpServletResponse response) {
    }

    @PostMapping("/refresh-token")
    public @ResponseBody AuthResponseDto refreshToken(@RequestBody TokensRequestDto tokensRequestDto) {
        return authService.refreshToken(tokensRequestDto.getAccessToken(), tokensRequestDto.getRefreshToken());
    }

    @PostMapping("/logout")
    public void logout(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        authService.logout(refreshToken);
    }

}
