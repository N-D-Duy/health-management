package com.example.health_management.domain.entities;

import com.example.health_management.common.shared.enums.ArticleCategory;
import com.example.health_management.common.shared.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "health_guides")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HealthArticle extends BaseEntity{
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ArticleCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ArticleStatus status;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = User.class)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private User user;
}
