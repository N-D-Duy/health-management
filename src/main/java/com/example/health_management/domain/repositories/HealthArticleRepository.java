package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.HealthArticle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthArticleRepository extends SoftDeleteRepository<HealthArticle, Long> {
    @Query("SELECT h FROM HealthArticle h WHERE h.user.id = :userId")
    HealthArticle findByUserId(Long userId);

    @Query("SELECT h FROM HealthArticle h WHERE h.user.id = :userId")
    List<HealthArticle> findAllByUserId(Long userId);
}