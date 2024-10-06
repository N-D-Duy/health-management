package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.HealthProvider;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthProviderRepository extends SoftDeleteRepository<HealthProvider, Long> {
}