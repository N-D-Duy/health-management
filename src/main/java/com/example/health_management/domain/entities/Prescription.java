package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "prescriptions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Prescription extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String diagnosis;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = User.class)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = Doctor.class)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Doctor doctor;

    private String notes;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL)
    private List<PrescriptionDetails> details = new ArrayList<>();

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL)
    private List<MedicalConditions> medicalConditions = new ArrayList<>();
}
