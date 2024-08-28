package com.example.health_management.domain.services;

import com.example.health_management.domain.entities.Account;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AccountRepository;
import com.example.health_management.domain.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtService {

    private UserRepository userRepository;
    private AccountRepository accountRepository;

    @Autowired
    public JwtService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public User extractUserFromToken() {
        try {
            UserDetails userDetails = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ///todo: namnx: use UserService to test async function
            User user = userRepository.findByAccount_Email(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
            return user;
        } catch (RuntimeException e) {
            log.error("User extract error: ", e);
            throw new RuntimeException("User extract error: ", e);
        }
    }
}
