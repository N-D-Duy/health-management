package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.DoctorSchedule;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DoctorScheduleRepository extends SoftDeleteRepository<DoctorSchedule, Long> {
    @Query("SELECT ds FROM DoctorSchedule ds WHERE ds.doctor.id = :doctorId AND ds.deletedAt IS NULL")
    List<DoctorSchedule> findAllByDoctorId(Long doctorId);

    @Modifying
    @Query("UPDATE DoctorSchedule ds SET ds.currentPatientCount = ds.currentPatientCount + :increase WHERE ds.id = :id")
    void updatePatientsCount(Long id, Integer increase);

    @Query("SELECT ds FROM DoctorSchedule ds WHERE ds.doctor.id = :doctorId AND ds.startTime = :startTime AND ds.endTime = :endTime AND ds.deletedAt IS NULL")
    DoctorSchedule findByTimes(Long doctorId, LocalDateTime startTime, LocalDateTime endTime);
}
