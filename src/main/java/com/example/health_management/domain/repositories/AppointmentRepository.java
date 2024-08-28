package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Set<Appointment> findByUser_Id(@NonNull Integer id);

    Optional<Appointment> findByIdAndUser_Id(@NonNull int id, @NonNull Integer id1);
}