package com.example.health_management.api.controllers;


import com.example.health_management.application.DTOs.auth.AuthResponseDto;
import com.example.health_management.application.DTOs.auth.TokensRequestDto;
import com.example.health_management.application.DTOs.auth.RegisterDto;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.guards.MyUserDetails;
import com.example.health_management.domain.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints for user authentication")
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;

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

    @GetMapping("/user")
    public @ResponseBody ResponseEntity<ApiResponse> getAuthenticatedUser() {
        try {
            MyUserDetails userDetails = jwtProvider.extractUserDetailsFromToken();

            ApiResponse response = new ApiResponse();
            response.setData(userDetails);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(response);
        }
    }
}
