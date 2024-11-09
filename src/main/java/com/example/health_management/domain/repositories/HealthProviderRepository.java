package com.example.health_management.domain.repositories;

import com.example.health_management.common.shared.enums.DoctorSpecialization;
import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.HealthProvider;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthProviderRepository extends SoftDeleteRepository<HealthProvider, Long> {
    @Query("SELECT hp FROM HealthProvider hp LEFT JOIN FETCH hp.doctors WHERE hp.id = :providerId")
    Optional<HealthProvider> findByIdWithDoctors(@Param("providerId") Long providerId);

    @Query("SELECT DISTINCT hp FROM HealthProvider hp " +
            "LEFT JOIN FETCH hp.doctors d " +
            "WHERE d.specialization = :specialization")
    List<HealthProvider> findAllWithDoctorsBySpecialization(
            @Param("specialization") DoctorSpecialization specialization);

    @Query(value = """
        SELECT DISTINCT hp.*
        FROM health_providers hp
        INNER JOIN doctors d ON d.health_provider_id = hp.id
        INNER JOIN doctor_schedules ds ON ds.doctor_id = d.id
        WHERE d.specialization = :specialization
        AND ds.start_time <= :startTime
        AND ds.end_time >= :endTime
        AND ds.current_patient_count < (
          CASE
              WHEN d.specialization = 'NEUROLOGIST' THEN 2
              WHEN d.specialization = 'PEDIATRICIAN' THEN 2
              WHEN d.specialization = 'DIETICIAN' THEN 3
              WHEN d.specialization = 'PSYCHOLOGIST' THEN 1
              ELSE 5
          END
          )
          ORDER BY hp.id;  
            )""", nativeQuery = true)
    List<HealthProvider> findDoctorsBySpecializationAvailableForTimes(@Param("specialization") String specialization, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}