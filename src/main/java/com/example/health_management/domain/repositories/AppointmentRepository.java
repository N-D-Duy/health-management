package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}