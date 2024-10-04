package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_metrics")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserMetrics extends BaseEntity{
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;


    private Long userId;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "height")
    private Double height;

    @Column(name = "age")
    private Integer age;

    @Column(name = "bmi")
    private Double bmi;
}
