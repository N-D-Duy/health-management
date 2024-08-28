package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "prescriptions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Prescription {
    //todo: namnx refractor later on
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String diagnosis;
    private String treatment;
    private LocalDate create_date;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = User.class)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User user;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,targetEntity = Medication.class)
    @JoinTable(name = "prescription_medications",
            joinColumns = @JoinColumn(name = "prescription_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id"))
    private Set<Medication> medications;
}
