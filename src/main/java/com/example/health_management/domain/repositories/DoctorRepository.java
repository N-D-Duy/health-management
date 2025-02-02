package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.Doctor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface DoctorRepository extends SoftDeleteRepository<Doctor, Long> {
    @Modifying
    @Query("UPDATE Doctor d SET d.healthProvider.id = :providerId WHERE d.id = :doctorId")
    int joinHealthProvider(@Param("doctorId") Long doctorId, @Param("providerId") Long providerId);

    @Modifying
    @Query("UPDATE Doctor d SET d.healthProvider = NULL WHERE d.id = :doctorId AND d.healthProvider.id = :providerId")
    int leaveHealthProvider(@Param("doctorId") Long doctorId, @Param("providerId") Long providerId);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Doctor d WHERE d.id = :doctorId AND d.healthProvider.id = :providerId")
    boolean isDoctorInHealthProvider(@Param("doctorId") Long doctorId, @Param("providerId") Long providerId);

    @Query(value = """
        SELECT * 
        FROM doctors d
        WHERE d.specialization = :specialization
        """, nativeQuery = true)
    List<Doctor> findBySpecialization(String specialization);

    @Query("SELECT DISTINCT d FROM Doctor d " +
            "LEFT JOIN FETCH d.schedules " +
            "WHERE d IN :doctors")
    List<Doctor> findDoctorsWithSchedules(@Param("doctors") Collection<Doctor> doctors);
}
