package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.HealthGuide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthGuideRepository extends JpaRepository<HealthGuide, Long> {
}