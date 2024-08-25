package com.example.health_management.application.security;

import com.example.health_management.application.guards.CustomAuthenticationFilter;
import com.example.health_management.application.guards.JwtAuthenticationFilter;
import com.example.health_management.domain.services.JwtService;
import com.example.health_management.domain.services.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final KeyService keyService;

    public SecurityConfig(JwtService jwtService, KeyService keyService) {
        this.jwtService = jwtService;
        this.keyService = keyService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/public/**").permitAll() // Public endpoints
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user/**").hasRole("USER")
                                .anyRequest().authenticated() // All other requests require authentication
                )
                .addFilterBefore(new CustomAuthenticationFilter(authenticationManager(http)), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(keyService, jwtService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationConfiguration authenticationConfiguration = http.getSharedObject(AuthenticationConfiguration.class);
        return authenticationConfiguration.getAuthenticationManager();
    }

}




