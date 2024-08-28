package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.PhysicalActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicalActivityRepository extends JpaRepository<PhysicalActivity, Long> {
}