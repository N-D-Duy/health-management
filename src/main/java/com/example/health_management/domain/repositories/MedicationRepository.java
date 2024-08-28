package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}