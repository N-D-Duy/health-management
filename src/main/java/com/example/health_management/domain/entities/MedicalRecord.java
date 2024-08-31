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
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    ///todo: ask about the last_name field
    private String note;
    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,targetEntity = Prescription.class,orphanRemoval = true)
    private Prescription prescription;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,targetEntity = User.class)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private User user;
    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,targetEntity = Appointment.class, orphanRemoval = true)
    private Appointment appointment;

}
