package com.example.health_management.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.health_management.domain.entities.Key;

@Repository
public interface KeyRepository extends JpaRepository<Key, Integer> {
    String findPublicKeyByUserId(Integer userId);
}
