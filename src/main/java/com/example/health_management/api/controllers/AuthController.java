package com.example.health_management.api.controllers;


import com.example.health_management.application.DTOs.auth.AuthResponse;
import com.example.health_management.application.DTOs.auth.RegisterDTO;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.domain.services.AccountService;
import com.example.health_management.domain.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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
    @io.swagger.v3.oas.annotations.Operation(
            summary = "Login user",
            description = "Authenticate a user with email, password, and notification key",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login request body",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    example = "{\"email\": \"john.doe@example.com\", \"password\": \"12332145\", \"notification_key\": \"fcm_token\"}"
                            )
                    )
            )
    )
    public void login(HttpServletResponse response) {

    }

    @PostMapping("/refresh-token")
    public @ResponseBody ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        AuthResponse data = authService.refreshToken(refreshToken);
        if (data == null) {
            return ResponseEntity.status(500).body(ApiResponse.<AuthResponse>builder().code(500).message("Invalid refresh token").build());
        }
        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder().code(200).data(authService.refreshToken(refreshToken)).message("Success").build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public @ResponseBody ResponseEntity<String> logout() {
        return ResponseEntity.ok(authService.logout());
    }

    @PostMapping("/active")
    public ResponseEntity<ApiResponse<String>> activeAccount(@Param("email") String email) {
        ApiResponse<String> response = authService.active(email) ? ApiResponse.<String>builder().code(200).data("Account activated").message("Success").build() : ApiResponse.<String>builder().code(500).data("Account not activated").message("Failed").build();
        return ResponseEntity.ok(response);
    }
}
