package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllergyRepository extends JpaRepository<Allergy, Long> {
}
