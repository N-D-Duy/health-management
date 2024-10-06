package com.example.health_management.api.controllers;


import java.util.Map;
import java.util.Objects;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.health_management.application.DTOs.auth.AuthResponse;
import com.example.health_management.application.DTOs.auth.RegisterDTO;
import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.guards.MyUserDetails;
import com.example.health_management.domain.services.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final CacheManager cacheManager;

    @PostMapping("/register")
    public @ResponseBody AuthResponse register(@RequestBody RegisterDTO registerDto) {
        return authService.register(registerDto);
    }

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
    public @ResponseBody AuthResponse refreshToken(@RequestBody Map<String, String> body) {
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
