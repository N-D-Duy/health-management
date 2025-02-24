package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.article.ArticleDTO;
import com.example.health_management.application.DTOs.article.CreateArticleRequest;
import com.example.health_management.application.DTOs.article.UpdateArticleRequest;
import com.example.health_management.application.DTOs.article_support.ArticleCommentDTO;
import com.example.health_management.application.DTOs.logging.LoggingDTO;
import com.example.health_management.application.mapper.ArticleMapper;
import com.example.health_management.common.shared.enums.LoggingType;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.entities.Article;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.ArticleRepository;
import com.example.health_management.domain.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;
    private final LoggingService loggingService;

    private final ArticleMediaService articleMediaService;
    private final ArticleCommentService articleCommentService;

    public ArticleDTO createArticle(CreateArticleRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ConflictException(".getMessage()User not found with ID: " + userId));
        Article article = articleMapper.createFromRequest(request);
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);

        articleMediaService.createArticleMedia(request.getMedia(), savedArticle);

        loggingService.saveLog(LoggingDTO.builder().message("User with " + userId + " created article").type(LoggingType.ARTICLE_CREATED).build());
        return articleMapper.toDTO(savedArticle);
    }

    public ArticleDTO updateArticle(UpdateArticleRequest request, Long articleId) {
        try {
            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new ConflictException(".getMessage()Health article not found with ID: " + articleId));

            Article updatedArticle = articleMapper.updateFromRequest(request, article);
            loggingService.saveLog(LoggingDTO.builder().message("Article with " + articleId + " updated").type(LoggingType.ARTICLE_UPDATED).build());
            return articleMapper.toDTO(articleRepository.save(updatedArticle));
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public List<ArticleDTO> getArticles() {
        return articleRepository.findAllActive().stream()
                .map(article -> {
                    ArticleDTO articleDTO = articleMapper.toDTO(article);

                    List<ArticleCommentDTO> rootComments = articleCommentService.buildCommentTree(articleDTO.getComments());
                    articleDTO.setComments(rootComments);

                    return articleDTO;
                })
                .collect(Collectors.toList());
    }

    public List<ArticleDTO> getArticlesByUserId(Long userId) {
        return articleRepository.findAllByUserId(userId).stream()
                .map(article -> {
                    ArticleDTO articleDTO = articleMapper.toDTO(article);

                    List<ArticleCommentDTO> rootComments = articleCommentService.buildCommentTree(articleDTO.getComments());
                    articleDTO.setComments(rootComments);

                    return articleDTO;
                })
                .collect(Collectors.toList());
    }


    public ArticleDTO getArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with ID: " + id));
        article.setViewCount(article.getViewCount() + 1);
        articleRepository.save(article);
        ArticleDTO articleDTO = articleMapper.toDTO(article);
        articleDTO.setComments(articleCommentService.buildCommentTree(articleDTO.getComments()));
        return articleDTO;
    }

    public void deleteArticle(Long id) {
        try {
            articleRepository.deleteById(id);
            loggingService.saveLog(LoggingDTO.builder().message("Article with id" + id + "deleted").type(LoggingType.ARTICLE_DELETED).build());
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }


}