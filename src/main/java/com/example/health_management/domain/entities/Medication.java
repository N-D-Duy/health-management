package com.example.health_management.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "medications")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image_url;
    private String description;
    private LocalDate mfgDate;
    private LocalDate expDate;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = MedicationType.class)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private MedicationType medicationType;
}
