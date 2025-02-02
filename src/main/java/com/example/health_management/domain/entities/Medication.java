package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "medications")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Medication extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image_url;
    private String description;
    private LocalDate mfgDate;
    private LocalDate expDate;
}
