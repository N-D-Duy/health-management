package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.Appointment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public interface AppointmentRepository extends SoftDeleteRepository<Appointment, Long> {
    Set<Appointment> findByUser_Id(@NonNull Long id);

    Optional<Appointment> findByIdAndUser_Id(@NonNull Long id, @NonNull Long id1);
}