package com.example.health_management.domain.cqrs.commands.impl.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefreshTokenCommand {
    private HttpServletRequest request;
    private HttpServletResponse response;
}
