package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends SoftDeleteRepository<Prescription, Long> {
    List<Prescription> findByUser_Id(@NonNull Long id);

    Optional<Prescription> findByUser_IdAndId(@NonNull Long id, @NonNull Long id1);
}