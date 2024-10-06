package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.UserMetrics;

public interface UserMetricsRepository extends SoftDeleteRepository<UserMetrics, Long> {
}
