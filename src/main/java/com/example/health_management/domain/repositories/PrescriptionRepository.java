package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    List<Prescription> findByUser_Id(@NonNull Integer id);

    Optional<Prescription> findByUser_IdAndId(@NonNull Integer id, @NonNull int id1);
}