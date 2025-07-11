package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository  extends SoftDeleteRepository<Account, Long> {
    Account findByUsername(String username);
    
    Account findByEmail(String email);

    @Modifying
    @Query("UPDATE Account a SET a.status = 'ACTIVE' WHERE a.email = :email")
    int activeAccount(String email);

    @Query("SELECT COUNT(a) > 0 FROM Account a WHERE a.user.id = :id AND a.role = 'DOCTOR'")
    boolean isDoctor(Long id);

    @Query("SELECT COUNT(a) > 0 FROM Account a WHERE a.user.id= :id AND a.role = 'USER'")
    boolean isPatient(Long id);
}
