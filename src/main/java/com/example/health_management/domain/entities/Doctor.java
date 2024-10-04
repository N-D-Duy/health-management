package com.example.health_management.domain.entities;


import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "doctors")
public class Doctor extends BaseEntity{
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "health_provider_id", unique = true)
    private HealthProvider healthProvider;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "experience")
    private Double experience;

    @Column(name = "qualification")
    private String qualification;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "about")
    private String about;
}
