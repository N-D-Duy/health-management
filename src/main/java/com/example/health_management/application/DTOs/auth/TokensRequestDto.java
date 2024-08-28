package com.example.health_management.application.DTOs.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TokensRequestDto {
    @NotNull(message = "Access token is required")
    @JsonProperty("access_token")
    private String accessToken;
    @NotNull(message = "Refresh token is required")
    @JsonProperty("refresh_token")
    private String refreshToken;
}
