package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends SoftDeleteRepository<Article, Long> {
    @Query("SELECT h FROM Article h WHERE h.user.id = :userId")
    Article findByUserId(Long userId);

    @Query("SELECT h FROM Article h WHERE h.user.id = :userId")
    List<Article> findAllByUserId(Long userId);
}