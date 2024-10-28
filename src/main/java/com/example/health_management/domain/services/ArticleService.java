package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.article.ArticleDTO;
import com.example.health_management.application.DTOs.article.CreateArticleRequest;
import com.example.health_management.application.DTOs.article.UpdateArticleRequest;
import com.example.health_management.application.mapper.ArticleMapper;
import com.example.health_management.application.mapper.ArticleMediaMapper;
import com.example.health_management.common.shared.enums.VoteType;
import com.example.health_management.domain.entities.Article;
import com.example.health_management.domain.entities.ArticleMedia;
import com.example.health_management.domain.entities.ArticleVote;
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

    private final ArticleMediaService articleMediaService;

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
                .map(articleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ArticleDTO> getArticlesByUserId(Long userId) {
        return articleRepository.findAllByUserId(userId).stream()
                .map(articleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }



}