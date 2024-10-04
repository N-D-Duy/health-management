package com.example.health_management.application.guards;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.health_management.application.DTOs.auth.AuthResponseDto;
import com.example.health_management.domain.entities.Payload;
import com.example.health_management.domain.repositories.AccountRepository;
import com.example.health_management.domain.repositories.KeyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LocalAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final KeyRepository keyRepository;
    private final AccountRepository accountRepository;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // Parse JSON body to get email and password
        Map<String, String> requestBody;
        try {
            requestBody = new ObjectMapper().readValue(request.getInputStream(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing request body", e);
        }

        String email = requestBody.get("email");
        String password = requestBody.get("password");
        String fcmToken = requestBody.get("notification_key");
        // Logging for debugging
        System.out.println("Attempting authentication for user: " + email);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
        try {
            Authentication authentication = authenticationManager.authenticate(authRequest);
            System.out.println("Authentication result: Success");
            //update notification key
            jwtProvider.updateFcmToken(email, fcmToken);
            return authentication;
        } catch (AuthenticationException e) {
            // Log the failure
            System.out.println("Authentication result: Failure - " + e.getMessage());
            throw e; // rethrow the exception to let Spring handle it
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        /*
        * gen tokens
        * first, get the user details
        * then get email from user details
        * finally generate the tokens from private key
        * */
        String email = ((UserDetails) authResult.getPrincipal()).getUsername();
        logger.warning("email: " + email);
        Long userId = accountRepository.findByEmail(email).getUser().getId();
        logger.warning("userId: " + userId);
        // Lấy privateKey từ email
        String privateKey = jwtProvider.getPrivateKeyByEmail(email);
        int version = jwtProvider.getVersionByEmail(email);
        jwtProvider.updateVersion(userId, ++version);


        // Tạo payload cho tokens
        Payload payload = new Payload();
        payload.setEmail(email);
        payload.setRole(((UserDetails) authResult.getPrincipal()).getAuthorities().iterator().next().getAuthority());
        payload.setId(userId);
        payload.setVersion(version);

        // Tạo tokens
        String accessToken = jwtProvider.generateAccessToken(payload, privateKey);
        String refreshToken = jwtProvider.generateRefreshToken(payload, privateKey);

        keyRepository.updateRefreshTokenByUserId(refreshToken, userId);

        // Gửi tokens về client
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build()));

        response.setStatus(HttpServletResponse.SC_OK);

    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        response.getWriter().write("Login failed: " + failed.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}

