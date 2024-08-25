package com.example.health_management.domain.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private LocalDate date;
    //todo: set optional to true for now in case of remote health provider
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = HealthProvider.class,optional = true)
    @JoinColumn(referencedColumnName = "id", nullable = true)
    private HealthProvider healthProvider;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = User.class,optional = false)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = ApppointmentType.class, optional = false)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private ApppointmentType apppointmentType;

}
