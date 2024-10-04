package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "countries")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Country {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
}
