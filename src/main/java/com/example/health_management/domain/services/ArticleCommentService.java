package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.article_support.ArticleCommentDTO;
import com.example.health_management.application.mapper.ArticleCommentMapper;
import com.example.health_management.domain.entities.Article;
import com.example.health_management.domain.entities.ArticleComment;
import com.example.health_management.domain.repositories.ArticleCommentRepository;
import com.example.health_management.domain.repositories.ArticleRepository;
import com.example.health_management.domain.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleCommentMapper articleCommentMapper;

    public ArticleCommentDTO addComment(Long articleId, Long userId, ArticleCommentDTO dto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with ID: " + articleId));

        ArticleComment comment = new ArticleComment();
        comment.setArticle(article);
        comment.setUser(userRepository.getReferenceById(userId));
        comment.setContent(dto.getContent());

        if (dto.getParentId() != null) {
            ArticleComment parentComment = articleCommentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent comment not found with ID: " + dto.getParentId()));
            comment.setParentComment(parentComment);
        }

        article.setCommentCount(article.getCommentCount() + 1);
        articleRepository.save(article);

        return articleCommentMapper.toDTO(articleCommentRepository.save(comment));
    }

    public ArticleComment updateComment(Long commentId, ArticleCommentDTO dto) {
        ArticleComment comment = articleCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + commentId));

        comment = articleCommentMapper.update(dto, comment);
        return articleCommentRepository.save(comment);
    }
}
