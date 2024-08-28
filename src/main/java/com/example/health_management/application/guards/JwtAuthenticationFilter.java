package com.example.health_management.application.guards;

import com.example.health_management.common.shared.enums.Role;
import com.example.health_management.domain.entities.Account;
import com.example.health_management.domain.services.KeyService;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final KeyService keyService;
    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(KeyService keyService, JwtProvider jwtProvider) {
        this.keyService = keyService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NotNull HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        final Logger logger = LoggerFactory.getLogger(this.getClass());

        String token = jwtProvider.extractToken(request);
        logger.info("Token: {}", token);
        if(token == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                        @Override
                        public Key resolveSigningKey(JwsHeader header, Claims claims) {
                            String email = claims.get("email", String.class);
                            String publicKeyPEM = jwtProvider.getPublicKeyByEmail(email);
                            if (publicKeyPEM == null) {
                                throw new RuntimeException("Public key not found for user: " + email);
                            }
                            try {
                                byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
                                X509EncodedKeySpec spec = new X509EncodedKeySpec(encoded);
                                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                                return keyFactory.generatePublic(spec);
                            } catch (Exception e) {
                                throw new RuntimeException("Error generating public key", e);
                            }
                        }
                    })
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Check if token is expired
            if (claims.getExpiration().before(new Date())) {
                logger.warn("Token is expired");
                SecurityContextHolder.clearContext();
                chain.doFilter(request, response);
                throw new RuntimeException("Token is expired");
            }

            // Get role from claims and convert to Role enum
            String roleName = claims.get("role", String.class); // Get the role from token claims
            Role role = Role.valueOf(roleName); // Convert roleName string to Role enum

            // Get authorities from Role
            List<SimpleGrantedAuthority> authorities = role.getAuthorities(); // Get all authorities including ROLE_ and permissions

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    Account.builder().username(claims.get("email", String.class)).build(), null, authorities

            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException e) {
            logger.warn("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("Invalid JWT token: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.warn("Invalid JWT signature: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unable to parse JWT token: {}", e.getMessage());
        }

        chain.doFilter(request, response);
    }
}