package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.HealthProvider;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthProviderRepository extends SoftDeleteRepository<HealthProvider, Long> {
    @Query("SELECT hp FROM HealthProvider hp LEFT JOIN FETCH hp.doctors WHERE hp.id = :providerId")
    Optional<HealthProvider> findByIdWithDoctors(@Param("providerId") Long providerId);
}