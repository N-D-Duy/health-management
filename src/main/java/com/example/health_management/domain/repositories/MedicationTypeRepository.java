package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.MedicationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationTypeRepository extends JpaRepository<MedicationType, Integer> {
}