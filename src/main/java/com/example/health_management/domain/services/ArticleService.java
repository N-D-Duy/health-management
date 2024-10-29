package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.article.ArticleDTO;
import com.example.health_management.application.DTOs.article.CreateArticleRequest;
import com.example.health_management.application.DTOs.article.UpdateArticleRequest;
import com.example.health_management.application.DTOs.article_support.ArticleCommentDTO;
import com.example.health_management.application.mapper.ArticleMapper;
import com.example.health_management.application.mapper.ArticleMediaMapper;
import com.example.health_management.common.shared.enums.VoteType;
import com.example.health_management.domain.entities.*;
import com.example.health_management.domain.repositories.ArticleRepository;
import com.example.health_management.domain.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;

    private final ArticleMediaService articleMediaService;
    private final ArticleCommentService articleCommentService;

    public ArticleDTO createArticle(CreateArticleRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));


        Article article = articleMapper.createFromRequest(request);
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);

        articleMediaService.createArticleMedia(request.getMedia(), savedArticle);

        return articleMapper.toDTO(savedArticle);
    }

    public ArticleDTO updateArticle(UpdateArticleRequest request, Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Health article not found with ID: " + articleId));

        Article updatedArticle = articleMapper.updateFromRequest(request, article);
        return articleMapper.toDTO(articleRepository.save(updatedArticle));
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
        articleRepository.deleteById(id);
    }


}