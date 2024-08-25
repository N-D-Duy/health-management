package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.ApppointmentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApppointmentTypeRepository extends JpaRepository<ApppointmentType, Integer> {
}