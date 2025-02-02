package com.example.health_management.application.DTOs.article_support;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ArticleCommentDTO implements Serializable {
    private Long id;
    private Long articleId;
    private Long userId;
    private String username; //ten thang comment
    private String userAvatar; //avatar cua thang comment
    private Long parentId; //id cua comment cha
    private String content;
    private Integer upvoteCount;
    private Integer downvoteCount;
    private List<ArticleCommentDTO> replies;
}
