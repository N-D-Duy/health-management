package com.example.health_management.domain.repositories.analytics;

import com.example.health_management.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface DataAnalysisRepository extends JpaRepository<User, Long> {
    @Query(value = """
    SELECT 
        COUNT(*) AS totalUser,
        SUM(CASE WHEN dp.id IS NULL THEN 1 ELSE 0 END) AS totalPatient,
        SUM(CASE WHEN dp.id IS NOT NULL THEN 1 ELSE 0 END) AS totalDoctor
    FROM 
        users u
    LEFT JOIN doctors dp ON u.id = dp.user_id
    WHERE 
        u.created_at BETWEEN :startDate AND :endDate
    """, nativeQuery = true)
    UserStatisticsProjection getUserStatistics(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query(value = """
    SELECT 
        gender,
        COUNT(*) AS count 
    FROM 
        users 
    WHERE 
        created_at BETWEEN :startDate AND :endDate AND gender IS NOT NULL
    GROUP BY 
        gender
    """, nativeQuery = true)
    List<GenderStatisticsProjection> getUserCountByGender(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query(value = """
    SELECT 
        status,
        COUNT(*) AS count 
    FROM 
        appointment_records 
    WHERE 
        created_at BETWEEN :startDate AND :endDate
    GROUP BY 
        status
    """, nativeQuery = true)
    List<AppointmentStatusProjection> getAppointmentCountByStatus(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query(value = """
    SELECT 
        COUNT(*) AS count
    FROM 
        articles 
    WHERE 
        created_at BETWEEN :startDate AND :endDate
    """, nativeQuery = true)
    Long getArticleCount(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
