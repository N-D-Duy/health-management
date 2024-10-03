package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {
    List<MedicalRecord> findByUser_Id(@NonNull Integer id);

    Optional<MedicalRecord> findByAppointment_Id(@NonNull int id);

    Optional<MedicalRecord> findByIdAndUser_Id(@NonNull int id, @NonNull Integer id1);

    Optional<MedicalRecord> findByAppointment_IdAndUser_Id(@NonNull int id, @NonNull Integer id1);
}