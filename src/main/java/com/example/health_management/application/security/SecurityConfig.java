package com.example.health_management.application.security;

import com.example.health_management.application.guards.JwtAuthenticationFilter;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.guards.LocalAuthenticationFilter;
import com.example.health_management.application.guards.RateLimitingFilter;
import com.example.health_management.common.Constants;
import com.example.health_management.domain.repositories.AccountRepository;
import com.example.health_management.domain.repositories.KeyRepository;
import com.example.health_management.domain.services.KeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final KeyService keyService;
    private final KeyRepository keyRepository;
    private final AccountRepository accountRepository;

    private final String[] WHITE_LIST = Constants.WHITE_LIST;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        final LocalAuthenticationFilter localAuthenticationFilter = new LocalAuthenticationFilter(authenticationManager(http), jwtProvider, keyRepository, accountRepository);
        localAuthenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(WHITE_LIST).permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .addFilterBefore(localAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(keyService, jwtProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }
}





