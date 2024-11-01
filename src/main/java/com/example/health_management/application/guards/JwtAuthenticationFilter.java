package com.example.health_management.application.guards;

import com.example.health_management.application.apiresponse.ApiResponse;
import com.example.health_management.common.shared.enums.Role;
import com.example.health_management.domain.services.KeyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
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

        String token = jwtProvider.extractToken(request);
        if(token == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtProvider.extractClaimsFromToken(token);
            if(claims == null) {
                ApiResponse responseWrapper = new ApiResponse(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or Expired JWT token", null);
                writeResponse(responseWrapper, response);
                return;
            }
            // Check if token is expired
            if (claims.getExpiration().before(new Date())) {
                SecurityContextHolder.clearContext();
                ApiResponse responseWrapper = new ApiResponse(HttpServletResponse.SC_UNAUTHORIZED, "Token expired", null);
                writeResponse(responseWrapper, response);
                return;
            }


            Double id = claims.get("id", Double.class);
            Long userId = id.longValue();
            Double version = claims.get("version", Double.class);
            Integer keyVersion = version.intValue();
            String roleName = claims.get("role", String.class);
            String email = claims.get("email", String.class);

            Role role = Role.valueOf(roleName); // Convert roleName string to Role enum

            //check if token is existing in the database (in the case of logout)
            if (!keyService.isTokenExist(userId.toString())) {
                ApiResponse responseWrapper = new ApiResponse(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token", null);
                writeResponse(responseWrapper, response);
                return;
            }
            // Get authorities from Role
            List<SimpleGrantedAuthority> authorities = role.getAuthorities(); // Get all authorities including ROLE_ and permissions

            MyUserDetails customUserDetails = new MyUserDetails(userId, email, authorities, keyVersion);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException e) {
            ApiResponse responseWrapper = new ApiResponse(HttpServletResponse.SC_UNAUTHORIZED, "Token expired", null);
            writeResponse(responseWrapper, response);
            return;
        } catch (UnsupportedJwtException e) {
            ApiResponse responseWrapper = new ApiResponse(HttpServletResponse.SC_UNAUTHORIZED, "Unsupported JWT token", null);
            writeResponse(responseWrapper, response);
            return;
        } catch (MalformedJwtException e) {
            ApiResponse responseWrapper = new ApiResponse(HttpServletResponse.SC_UNAUTHORIZED, "Malformed JWT token", null);
            writeResponse(responseWrapper, response);
            return;
        } catch (SignatureException e) {
            ApiResponse responseWrapper = new ApiResponse(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT signature", null);
            writeResponse(responseWrapper, response);
            return;
        } catch (IllegalArgumentException e) {
            ApiResponse responseWrapper = new ApiResponse(HttpServletResponse.SC_UNAUTHORIZED, "JWT claims string is empty", null);
            writeResponse(responseWrapper, response);
            return;
        } catch (Exception e) {
            ApiResponse responseWrapper = new ApiResponse(HttpServletResponse.SC_UNAUTHORIZED, "Unable to parse JWT token", null);
            writeResponse(responseWrapper, response);
            return;
        }
        chain.doFilter(request, response);
    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        String[] patterns = {"/api/v1/core/auth/register"};
//        AntPathMatcher pathMatcher = new AntPathMatcher();
//        for (String pattern : patterns) {
//            if (pathMatcher.match(pattern, request.getServletPath())) {
//                return true;
//            }
//        }
//        return false;    }


    private void writeResponse(ApiResponse apiResponse, HttpServletResponse response) throws IOException {
        response.setStatus(apiResponse.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiResponse));
        response.getWriter().flush();
    }
}