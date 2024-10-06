package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository  extends SoftDeleteRepository<Account, Long> {
    Account findByUsername(String username);
    
    Account findByEmail(String email);
}
