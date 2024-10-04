package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
