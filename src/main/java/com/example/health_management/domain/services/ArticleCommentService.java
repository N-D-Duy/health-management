package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.article_support.ArticleCommentDTO;
import com.example.health_management.application.DTOs.logging.LoggingDTO;
import com.example.health_management.application.mapper.ArticleCommentMapper;
import com.example.health_management.common.shared.enums.LoggingType;
import com.example.health_management.domain.entities.Article;
import com.example.health_management.domain.entities.ArticleComment;
import com.example.health_management.domain.repositories.ArticleCommentRepository;
import com.example.health_management.domain.repositories.ArticleRepository;
import com.example.health_management.domain.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleCommentService {
    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleCommentMapper articleCommentMapper;
    private final LoggingService loggingService;

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
        loggingService.saveLog(LoggingDTO.builder().message("Comment added to article with ID: " + articleId + " By user ID: " + userId).type(LoggingType.ARTICLE_COMMENT_CREATED).build());

        return articleCommentMapper.toDTO(articleCommentRepository.save(comment));
    }

    public ArticleComment updateComment(Long commentId, ArticleCommentDTO dto) {
        ArticleComment comment = articleCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with ID: " + commentId));

        comment = articleCommentMapper.update(dto, comment);
        return articleCommentRepository.save(comment);
    }

    //build comment tree
    protected List<ArticleCommentDTO> buildCommentTree(List<ArticleCommentDTO> comments) {
        Map<Long, ArticleCommentDTO> commentMap = new HashMap<>();
        List<ArticleCommentDTO> rootComments = new ArrayList<>();


        comments.forEach(comment -> {
            comment.setReplies(new ArrayList<>());
            commentMap.put(comment.getId(), comment);
        });

        comments.forEach(comment -> {
            if (comment.getParentId() == null) {
                // if it is a root comment, add it to the rootComments list
                rootComments.add(comment);
            } else {
                // if not, add it as a reply to its parent comment
                ArticleCommentDTO parentComment = commentMap.get(comment.getParentId());
                if (parentComment != null) {
                    parentComment.getReplies().add(comment);
                }
            }
        });

        return rootComments;
    }
}
