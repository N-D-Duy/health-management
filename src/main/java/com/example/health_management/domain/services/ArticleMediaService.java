package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.article_support.ArticleMediaDTO;
import com.example.health_management.application.mapper.ArticleMediaMapper;
import com.example.health_management.domain.entities.Article;
import com.example.health_management.domain.entities.ArticleMedia;
import com.example.health_management.domain.repositories.ArticleMediaRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleMediaService {
    private final ArticleMediaMapper articleMediaMapper;
    private final ArticleMediaRepository articleMediaRepository;

    protected void createArticleMedia(@NonNull List<ArticleMediaDTO> media, Article article) {
        List<ArticleMedia> articleMediaList = media.stream()
                .map(dto -> articleMediaMapper.toEntity(dto, article))
                .collect(Collectors.toList());
        article.setMedia(articleMediaList);
        articleMediaRepository.saveAll(articleMediaList);
    }

    protected void updateArticleMedia(@NonNull List<ArticleMediaDTO> media, @NonNull Article article) {
        List<ArticleMedia> articleMediaList = media.stream()
                .map(dto -> articleMediaMapper.toEntity(dto, article))
                .collect(Collectors.toList());
        article.setMedia(articleMediaList);
        articleMediaRepository.saveAll(articleMediaList);
    }
}
