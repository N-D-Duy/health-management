package com.example.health_management.application.DTOs.article;

import com.example.health_management.application.DTOs.article_support.ArticleMediaDTO;
import com.example.health_management.common.shared.enums.ArticleCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArticleRequest {
    private Long id;
    private String title;
    private String content;
    private ArticleCategory category;
    private List<ArticleMediaDTO> media;
}