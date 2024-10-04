package com.example.health_management.domain.entities;

import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.enums.AppointmentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "appointments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Appointment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalDate scheduledAt;


    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE},targetEntity = HealthProvider.class,optional = true)
    @JoinColumn(referencedColumnName = "id")
    private HealthProvider healthProvider;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE},targetEntity = User.class,optional = false)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE},targetEntity = Doctor.class,optional = true)
    @JoinColumn(referencedColumnName = "id")
    private Doctor doctor;

    @Column(name = "note")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type")
    private AppointmentType appointmentType;
}
