package com.example.health_management.application.DTOs.auth;

import com.example.health_management.application.DTOs.user.response.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
public class AuthResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    private UserDTO user;
}
