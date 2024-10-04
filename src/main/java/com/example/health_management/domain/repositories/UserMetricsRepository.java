package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.UserMetrics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMetricsRepository extends JpaRepository<UserMetrics, Long> {
}
