package com.example.health_management.domain.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "medication_types")
public class MedicationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
}
