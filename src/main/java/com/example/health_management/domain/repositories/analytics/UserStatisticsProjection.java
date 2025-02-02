package com.example.health_management.domain.repositories.analytics;

public interface UserStatisticsProjection {
    Long getTotalUser();
    Long getTotalPatient();
    Long getTotalDoctor();
}
