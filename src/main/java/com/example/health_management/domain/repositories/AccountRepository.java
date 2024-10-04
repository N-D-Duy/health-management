package com.example.health_management.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.health_management.domain.entities.Account;

@Repository
public interface AccountRepository  extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
    
    Account findByEmail(String email);
}
