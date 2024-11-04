package com.example.health_management.domain.repositories;

import com.example.health_management.common.shared.enums.LoggingType;
import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.Logging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoggingRepository extends SoftDeleteRepository<Logging, Long> {
    @Query(
            value = "SELECT * FROM logging WHERE type = :type",
            nativeQuery = true
    )
    List<Logging> findByType(LoggingType type);
}
