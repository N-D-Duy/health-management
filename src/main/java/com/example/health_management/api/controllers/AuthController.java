package com.example.health_management.api.controllers;


import com.example.health_management.application.DTOs.auth.RegisterDto;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public @ResponseBody User register(@RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }
}
