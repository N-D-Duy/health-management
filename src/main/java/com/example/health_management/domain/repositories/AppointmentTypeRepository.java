package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.AppointmentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, Integer> {
}