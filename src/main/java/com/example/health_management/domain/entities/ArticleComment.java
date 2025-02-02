package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article_comments")
@Getter
@Setter
public class ArticleComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", referencedColumnName = "id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private ArticleComment parentComment; // Cho phép nested comments

    @OneToMany(mappedBy = "parentComment")
    private List<ArticleComment> replies = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer upvoteCount = 0;
    private Integer downvoteCount = 0;
}
