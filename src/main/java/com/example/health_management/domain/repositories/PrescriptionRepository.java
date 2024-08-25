package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}