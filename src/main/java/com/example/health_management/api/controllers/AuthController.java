package com.example.health_management.api.controllers;


import com.example.health_management.application.DTOs.auth.AuthResponseDto;
import com.example.health_management.application.DTOs.auth.TokensRequestDto;
import com.example.health_management.application.DTOs.auth.RegisterDto;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.guards.MyUserDetails;
import com.example.health_management.domain.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final CacheManager cacheManager;

    @PostMapping("/register")
    public @ResponseBody AuthResponseDto register(@RequestBody RegisterDto registerDto) {
        return authService.register(registerDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Cacheable(value = "health-management", key = "'health'")
    @GetMapping("/health")
    public @ResponseBody ApiResponse health() {
        if(Objects.requireNonNull(cacheManager.getCache("health-management")).get("health") != null) {
            ApiResponse response = new ApiResponse();
            response.setData("OK");
            return response;
        }
        return new ApiResponse(HttpServletResponse.SC_OK, "OK", null);
    }

    @PostMapping("/login")
    public void login(HttpServletResponse response) {
    }

    @PostMapping("/refresh-token")
    public @ResponseBody AuthResponseDto refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        return authService.refreshToken(refreshToken);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        authService.logout(refreshToken);
    }

    @Cacheable(value = "user", key = "'getAuthenticatedUser'")
    @GetMapping("/user")
    public @ResponseBody ApiResponse getAuthenticatedUser() {
        ApiResponse cachedResponse = Objects.requireNonNull(cacheManager.getCache("health-management")).get("getAuthenticatedUser", ApiResponse.class);
        if (cachedResponse != null) {
            ApiResponse response = new ApiResponse();
            response.setData(cachedResponse.getData());
            return response;
        }
        try {
            MyUserDetails userDetails = jwtProvider.extractUserDetailsFromToken();

            ApiResponse response = new ApiResponse();
            response.setData(userDetails);

            return response;
        } catch (Exception e) {
            return new ApiResponse(HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        }
    }
}
