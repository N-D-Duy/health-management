package com.example.health_management.application.guards;

import com.example.health_management.domain.entities.Account;
import com.example.health_management.domain.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        final Account account = accountRepository.findByEmail(email);
        logger.info("executing loadUserByUsername");

        if (account == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return User.withUsername(account.getEmail())
                .password(account.getPassword())
                .authorities(account.getRole().name())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
