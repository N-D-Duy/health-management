package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.HealthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthProviderTypeRepository extends JpaRepository<HealthProviderType, Integer> {
}