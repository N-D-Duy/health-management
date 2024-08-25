package com.example.health_management.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.health_management.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
