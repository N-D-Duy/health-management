package com.example.health_management.domain.entities;

import com.example.health_management.domain.entities.User;
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
public class HealthGuide {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private LocalDate createdDate;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = User.class)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private User user;
}
