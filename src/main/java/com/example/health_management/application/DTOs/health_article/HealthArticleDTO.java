package com.example.health_management.application.DTOs.health_article;

import lombok.Data;

import java.io.Serializable;

@Data
public class HealthArticleDTO implements Serializable {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String status;
    private Long userId;
}
