package com.example.health_management.api.controllers;


import com.example.health_management.application.DTOs.auth.AuthResponseDto;
import com.example.health_management.application.DTOs.auth.LoginDto;
import com.example.health_management.application.DTOs.auth.RegisterDto;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
}
