package com.example.health_management.application.DTOs.article_support;

import com.example.health_management.common.shared.enums.VoteType;
import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleVoteDTO implements Serializable {
    private Long id;
    private Long articleId;
    private Long userId;
    private Long username;
    private VoteType type;
}
