package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.HealthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthProviderTypeRepository extends JpaRepository<HealthProviderType, Integer> {
}