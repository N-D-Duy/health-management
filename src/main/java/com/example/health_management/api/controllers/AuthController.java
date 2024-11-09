package com.example.health_management.api.controllers;


import com.example.health_management.application.DTOs.auth.AuthResponse;
import com.example.health_management.application.DTOs.auth.RegisterDTO;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.domain.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public @ResponseBody ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        AuthResponse data = authService.refreshToken(refreshToken);
        if(data == null){
            return ResponseEntity.status(500).body(ApiResponse.<AuthResponse>builder().code(500).message("Invalid refresh token").build());
        }
        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder().code(200).data(authService.refreshToken(refreshToken)).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public @ResponseBody ResponseEntity<String> logout() {
        return ResponseEntity.ok(authService.logout());
    }
}
