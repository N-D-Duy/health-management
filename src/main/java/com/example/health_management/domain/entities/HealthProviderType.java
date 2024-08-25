package com.example.health_management.domain.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "provider_types")
public class HealthProviderType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String typeName;
    private String typeDescription;
}
