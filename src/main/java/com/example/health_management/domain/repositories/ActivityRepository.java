package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
}