package com.example.health_management.domain.cache.services;

import com.example.health_management.application.DTOs.article.ArticleDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ArticleCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String ALL_ARTICLES_KEY = "all:articles";
    private static final String USER_ARTICLES_KEY = "user:article:";
    private static final String ARTICLE_KEY_PREFIX = "article:";
    private static final Duration ARTICLE_EXPIRATION = Duration.ofDays(1);

    public void cacheArticle(String articleId, ArticleDTO article) {
        try {
            if (article == null) {
                redisTemplate.delete(ARTICLE_KEY_PREFIX + articleId);
                return;
            }
            String cacheKey = ARTICLE_KEY_PREFIX + articleId;
            String articleJson = objectMapper.writeValueAsString(article);
            redisTemplate.opsForValue().set(cacheKey, articleJson, ARTICLE_EXPIRATION);
        } catch (Exception e) {
            log.error("Failed to cache article: {}", articleId, e);
        }
    }

    public ArticleDTO getCachedArticle(String articleId) {
        try {
            String cacheKey = ARTICLE_KEY_PREFIX + articleId;
            Object cachedValue = redisTemplate.opsForValue().get(cacheKey);
            
            if (cachedValue == null) {
                return null;
            }
            
            if (cachedValue instanceof String) {
                return objectMapper.readValue((String) cachedValue, ArticleDTO.class);
            }
            
            return null;
        } catch (Exception e) {
            log.error("Failed to retrieve cached article: {}", articleId, e);
            redisTemplate.delete(ARTICLE_KEY_PREFIX + articleId);
            return null;
        }
    }

    public void cacheAllArticles(List<ArticleDTO> articles) {
        try {
            if (articles == null) {
                redisTemplate.delete(ALL_ARTICLES_KEY);
                return;
            }
            String articlesJson = objectMapper.writeValueAsString(articles);
            redisTemplate.opsForValue().set(ALL_ARTICLES_KEY, articlesJson, ARTICLE_EXPIRATION);
        } catch (Exception e) {
            log.error("Failed to cache all articles", e);
        }
    }

    public List<ArticleDTO> getCachedAllArticles() {
        try {
            Object cachedValue = redisTemplate.opsForValue().get(ALL_ARTICLES_KEY);
            
            if (cachedValue == null) {
                return null;
            }
            
            if (cachedValue instanceof String) {
                return objectMapper.readValue((String) cachedValue, new TypeReference<List<ArticleDTO>>() {});
            }
            
            return null;
        } catch (Exception e) {
            log.error("Failed to retrieve all cached articles", e);
            invalidateAllArticlesCache();
            return null;
        }
    }
    
    public void cacheUserArticles(Long userId, List<ArticleDTO> articles) {
        try {
            if (articles == null) {
                redisTemplate.delete(USER_ARTICLES_KEY + userId);
                return;
            }
            String articlesJson = objectMapper.writeValueAsString(articles);
            redisTemplate.opsForValue().set(USER_ARTICLES_KEY + userId, articlesJson, ARTICLE_EXPIRATION);
        } catch (Exception e) {
            log.error("Failed to cache user articles for user: {}", userId, e);
        }
    }

    public List<ArticleDTO> getCachedUserArticles(Long userId) {
        try {
            Object cachedValue = redisTemplate.opsForValue().get(USER_ARTICLES_KEY + userId);
            
            if (cachedValue == null) {
                return null;
            }
            
            if (cachedValue instanceof String) {
                return objectMapper.readValue((String) cachedValue, new TypeReference<List<ArticleDTO>>() {});
            }
            
            return null;
        } catch (Exception e) {
            log.error("Failed to retrieve cached user articles for user: {}", userId, e);
            redisTemplate.delete(USER_ARTICLES_KEY + userId);
            return null;
        }
    }

    public void invalidateAllArticlesCache() {
        redisTemplate.delete(ALL_ARTICLES_KEY);
    }
    
    public void invalidateUserArticlesCache(Long userId) {
        redisTemplate.delete(USER_ARTICLES_KEY + userId);
    }

    public void invalidateArticleCache(String articleId) {
        redisTemplate.delete(ARTICLE_KEY_PREFIX + articleId);
    }
}
