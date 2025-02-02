package com.example.health_management.application.DTOs.article_support;

import com.example.health_management.common.shared.enums.MediaType;
import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleMediaDTO implements Serializable {
    private Long id;
    private Long articleId;
    private MediaType type;
    private String url;
    private String description;
    private Integer orderIndex;
}
