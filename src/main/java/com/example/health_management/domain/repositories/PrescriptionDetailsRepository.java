package com.example.health_management.domain.repositories;

import com.example.health_management.domain.entities.PrescriptionDetails;
import io.micrometer.common.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionDetailsRepository extends JpaRepository<PrescriptionDetails, Long> {
}
