package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.AppointmentRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRecordRepository extends SoftDeleteRepository<AppointmentRecord, Long> {
//    List<AppointmentRecord> findByUser_Id(@NonNull Long id);
//
//    Optional<AppointmentRecord> findByAppointment_Id(@NonNull Long id);
//
//    Optional<AppointmentRecord> findByIdAndUser_Id(@NonNull Long id, @NonNull Long id1);
//
//    Optional<AppointmentRecord> findByAppointment_IdAndUser_Id(@NonNull Long id, @NonNull Long id1);

    @Query("SELECT a FROM AppointmentRecord a WHERE a.user.id = :id")
    List<AppointmentRecord> findByUserId(@NonNull Long id);
}