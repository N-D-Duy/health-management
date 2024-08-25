package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "provider_types")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HealthProviderType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String typeName;
    private String typeDescription;
}
