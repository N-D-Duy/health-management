package com.example.health_management.domain.entities;

import com.example.health_management.common.shared.enums.ArticleCategory;
import com.example.health_management.common.shared.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Table(name = "articles")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Article extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private ArticleCategory category;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleVote> votes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleComment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleMedia> media = new ArrayList<>();

    // Các trường thống kê
    @Builder.Default
    private Integer upVoteCount = 0;

    @Builder.Default
    private Integer downVoteCount = 0;

    @Builder.Default
    private Integer commentCount = 0;

    @Builder.Default
    private Integer viewCount = 0;
}
