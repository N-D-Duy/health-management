package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.TelemedicineSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelemedicineSessionRepository extends JpaRepository<TelemedicineSession, Long> {
}