package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.MedicationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRecordRepository extends JpaRepository<MedicationRecord, Long> {
}