package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prescription_details")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrescriptionDetails {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    @ManyToOne
    @JoinColumn(name = "medication_id")
    private Medication medication;

    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;
}
