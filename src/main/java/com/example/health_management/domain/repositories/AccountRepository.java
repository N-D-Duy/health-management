package com.example.health_management.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.health_management.domain.entities.Account;

@Repository
public interface AccountRepository  extends JpaRepository<Account, Integer> {    
}
