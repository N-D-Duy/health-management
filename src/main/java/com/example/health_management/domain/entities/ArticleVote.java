package com.example.health_management.domain.entities;

import com.example.health_management.common.shared.enums.VoteType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "article_votes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"article_id", "user_id"}))
@Getter
@Setter
public class ArticleVote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", referencedColumnName = "id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private VoteType type; // UPVOTE, DOWNVOTE
}
