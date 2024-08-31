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
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String image_url;
    private String description;
    private LocalDate mfgDate;
    private LocalDate expDate;
    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE, CascadeType.PERSIST},targetEntity = MedicationType.class)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private MedicationType medicationType;
}
