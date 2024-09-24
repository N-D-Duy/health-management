package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "medication_types")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MedicationType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
}
