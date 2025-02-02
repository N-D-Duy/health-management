package com.example.health_management.application.DTOs.article;

import com.example.health_management.application.DTOs.article_support.ArticleMediaDTO;
import com.example.health_management.common.shared.enums.ArticleCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateArticleRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private ArticleCategory category;

    private List<ArticleMediaDTO> media;
}