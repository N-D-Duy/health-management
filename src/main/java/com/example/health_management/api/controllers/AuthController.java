package com.example.health_management.api.controllers;


import com.example.health_management.application.DTOs.auth.RegisterDto;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public @ResponseBody User register(@RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }

    @GetMapping("/health")
    public @ResponseBody String health() {
        return "OK";
    }
}
