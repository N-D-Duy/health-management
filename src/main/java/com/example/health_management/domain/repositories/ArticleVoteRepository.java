package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.ArticleVote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleVoteRepository extends SoftDeleteRepository<ArticleVote, Long> {
    @Query("SELECT v FROM ArticleVote v WHERE v.article.id = :articleId AND v.user.id = :userId")
    Optional<ArticleVote> findByArticleIdAndUserId(Long articleId, Long userId);

    @Modifying
    @Query("DELETE FROM ArticleVote v WHERE v.id = :id")
    void deletePermanentlyById(Long id);
}
