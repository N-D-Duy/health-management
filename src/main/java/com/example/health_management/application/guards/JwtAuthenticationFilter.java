package com.example.health_management.application.guards;

import com.example.health_management.domain.services.JwtService;
import com.example.health_management.domain.services.KeyService;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final KeyService keyService;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(KeyService keyService, JwtService jwtService) {
        this.keyService = keyService;
        this.jwtService = jwtService;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final Logger logger = LoggerFactory.getLogger(this.getClass());

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                        @Override
                        public Key resolveSigningKey(JwsHeader header, Claims claims) {
                            int userId = Integer.parseInt(claims.getSubject());
                            String publicKeyPEM = keyService.findPublicKeyByUserId(userId);
                            if (publicKeyPEM == null) {
                                throw new RuntimeException("Public key not found for user: " + userId);
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
                return;
            }


            String role = claims.get("role", String.class); // Get the single role
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), null, authorities
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