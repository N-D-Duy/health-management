package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionTypeRepository extends JpaRepository<SessionType, Integer> {
}