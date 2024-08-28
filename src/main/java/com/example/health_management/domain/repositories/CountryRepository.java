package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}