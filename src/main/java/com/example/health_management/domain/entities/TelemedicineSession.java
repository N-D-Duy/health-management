package com.example.health_management.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "telemedicine_sessions")
public class TelemedicineSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sessionNotes;
    private LocalDate sessionDate;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = User.class)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = HealthProvider.class)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private HealthProvider healthProvider;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = SessionType.class)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private SessionType sessionType;
}
