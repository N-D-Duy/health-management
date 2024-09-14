package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medication_records")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MedicationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String note;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = Prescription.class)
    private Prescription prescription;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = User.class)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private User user;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = Appointment.class)
    private Appointment appointment;

}
