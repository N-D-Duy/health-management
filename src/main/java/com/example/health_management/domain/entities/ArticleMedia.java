package com.example.health_management.domain.entities;

import com.example.health_management.common.shared.enums.MediaType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "article_media")
@Getter
@Setter
public class ArticleMedia extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", referencedColumnName = "id", nullable = false)
    private Article article;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    private String url;
    private String description;
    private Integer orderIndex; // Thứ tự hiển thị
}