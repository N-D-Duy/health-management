package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.DoctorSchedule;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorScheduleRepository extends SoftDeleteRepository<DoctorSchedule, Long> {
    @Query("SELECT ds FROM DoctorSchedule ds WHERE ds.doctor.id = :doctorId AND ds.deletedAt IS NULL")
    List<DoctorSchedule> findAllByDoctorId(Long doctorId);
    @Modifying
    @Query("UPDATE DoctorSchedule ds SET ds.currentPatientCount = :patientsCount WHERE ds.id = :id")
    void updatePatientsCount(Long id, Integer patientsCount);
}
