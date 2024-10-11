package com.example.health_management.domain.entities;

import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.enums.AppointmentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "appointment_records")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppointmentRecord extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String note;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,targetEntity = Prescription.class,orphanRemoval = true)
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,targetEntity = User.class)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id",nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,targetEntity = HealthProvider.class)
    @JoinColumn(name = "health_provider_id", referencedColumnName = "id",nullable = false)
    private HealthProvider healthProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type")
    private AppointmentType appointmentType;

    private LocalDate scheduledAt;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}
