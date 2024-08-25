package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "health_providers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HealthProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String providerName;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = HealthProviderType.class)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private HealthProviderType healthProviderType;
}
