package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends SoftDeleteRepository<User, Long> {
    Optional<User> findByAccount_Email(@NonNull String email);

    @Query("SELECT u FROM User u JOIN FETCH u.account WHERE u.account.email = :email AND u.deletedAt IS NULL")
    User findActiveByEmail(@NonNull String email);
}
