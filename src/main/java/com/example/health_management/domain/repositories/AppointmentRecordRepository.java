package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.AppointmentRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRecordRepository extends SoftDeleteRepository<AppointmentRecord, Long> {
    @Query("SELECT a FROM AppointmentRecord a WHERE a.user.id = :id")
    List<AppointmentRecord> findByUserId(@NonNull Long id);

    @Query("SELECT a FROM AppointmentRecord a WHERE a.doctor.id = :id")
    List<AppointmentRecord> findByDoctorId(@NonNull Long id);

    @Query("""
                SELECT a FROM AppointmentRecord a
                WHERE a.user.id = :userId
                AND a.depositStatus = 'HOLD'
                AND a.deletedAt IS NULL
                ORDER BY a.createdAt DESC
            """)
    Optional<AppointmentRecord> findLatestHeldDepositByUserId(@Param("userId") Long userId);

    @Query("""
                SELECT COUNT(a) > 0 FROM AppointmentRecord a
                WHERE a.user.id = :userId
                AND a.depositStatus = 'HOLD'
                AND a.deletedAt IS NULL
            """)
    boolean existsHeldDepositByUserId(@Param("userId") Long userId);
}