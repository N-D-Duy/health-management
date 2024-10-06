package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.HealthArticle;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthArticleRepository extends SoftDeleteRepository<HealthArticle, Long> {
}