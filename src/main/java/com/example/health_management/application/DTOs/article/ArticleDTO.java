package com.example.health_management.application.DTOs.article;

import com.example.health_management.application.DTOs.article_support.ArticleCommentDTO;
import com.example.health_management.application.DTOs.article_support.ArticleMediaDTO;
import com.example.health_management.application.DTOs.article_support.ArticleVoteDTO;
import com.example.health_management.common.shared.enums.ArticleCategory;
import com.example.health_management.common.shared.enums.ArticleStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ArticleDTO implements Serializable {
    private Long id;
    private String title;
    private String content;
    private ArticleCategory category;
    private ArticleStatus status;

    private Long userId;
    private String username;
    private String userAvatar;

    private List<ArticleVoteDTO> votes;
    private List<ArticleCommentDTO> comments;
    private List<ArticleMediaDTO> media;

    private Integer upVoteCount;
    private Integer downVoteCount;
    private Integer commentCount;
    private Integer viewCount;
}
