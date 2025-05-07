package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.article.ArticleDTO;
import com.example.health_management.application.DTOs.article.CreateArticleRequest;
import com.example.health_management.application.DTOs.article.UpdateArticleRequest;
import com.example.health_management.application.DTOs.article_support.ArticleCommentDTO;
import com.example.health_management.application.mapper.ArticleMapper;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.cache.services.ArticleCacheService;
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
    private final ArticleCacheService articleCacheService;

    private final ArticleMediaService articleMediaService;
    private final ArticleCommentService articleCommentService;

    public ArticleDTO createArticle(CreateArticleRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ConflictException(".getMessage()User not found with ID: " + userId));
        Article article = articleMapper.createFromRequest(request);
        article.setUser(user);
        Article savedArticle = articleRepository.save(article);

        articleMediaService.createArticleMedia(request.getMedia(), savedArticle);

        articleCacheService.invalidateAllArticlesCache();
        articleCacheService.invalidateUserArticlesCache(userId);

        return articleMapper.toDTO(savedArticle);
    }

    public ArticleDTO updateArticle(UpdateArticleRequest request, Long articleId) {
        try {
            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new ConflictException(".getMessage()Health article not found with ID: " + articleId));

            Long userId = article.getUser().getId();
            Article updatedArticle = articleMapper.updateFromRequest(request, article);
            ArticleDTO updatedArticleDTO = articleMapper.toDTO(articleRepository.save(updatedArticle));


            articleCacheService.cacheArticle(articleId.toString(), updatedArticleDTO);
            articleCacheService.invalidateAllArticlesCache();
            articleCacheService.invalidateUserArticlesCache(userId);

            return updatedArticleDTO;
        } catch (ConflictException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public List<ArticleDTO> getArticles() {

        List<ArticleDTO> cachedArticles = articleCacheService.getCachedAllArticles();
        if (cachedArticles != null) {
            return cachedArticles;
        }

        List<ArticleDTO> articles = articleRepository.findAllActive().stream()
                .map(article -> {
                    ArticleDTO articleDTO = articleMapper.toDTO(article);
                    List<ArticleCommentDTO> rootComments = articleCommentService.buildCommentTree(articleDTO.getComments());
                    articleDTO.setComments(rootComments);
                    return articleDTO;
                })
                .collect(Collectors.toList());

        articleCacheService.cacheAllArticles(articles);
        return articles;
    }

    public List<ArticleDTO> getArticlesByUserId(Long userId) {

        List<ArticleDTO> cachedUserArticles = articleCacheService.getCachedUserArticles(userId);
        if (cachedUserArticles != null) {
            return cachedUserArticles;
        }

        List<ArticleDTO> userArticles = articleRepository.findAllByUserId(userId).stream()
                .map(article -> {
                    ArticleDTO articleDTO = articleMapper.toDTO(article);
                    List<ArticleCommentDTO> rootComments = articleCommentService.buildCommentTree(articleDTO.getComments());
                    articleDTO.setComments(rootComments);
                    return articleDTO;
                })
                .collect(Collectors.toList());

        articleCacheService.cacheUserArticles(userId, userArticles);
        return userArticles;
    }

    public ArticleDTO getArticle(Long id) {

        ArticleDTO cachedArticle = articleCacheService.getCachedArticle(id.toString());
        if (cachedArticle != null) {
            return cachedArticle;
        }


        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with ID: " + id));
        article.setViewCount(article.getViewCount() + 1);
        articleRepository.save(article);

        ArticleDTO articleDTO = articleMapper.toDTO(article);
        articleDTO.setComments(articleCommentService.buildCommentTree(articleDTO.getComments()));


        articleCacheService.cacheArticle(id.toString(), articleDTO);

        return articleDTO;
    }

    public void deleteArticle(Long id) {
        try {
            Article article = articleRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Article not found with ID: " + id));
            Long userId = article.getUser().getId();

            articleRepository.deleteById(id);


            articleCacheService.cacheArticle(id.toString(), null);
            articleCacheService.invalidateAllArticlesCache();
            articleCacheService.invalidateUserArticlesCache(userId);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }
}
