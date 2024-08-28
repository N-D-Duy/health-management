package com.example.health_management.domain.entities;

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
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private LocalDate date;
    //todo: set optional to true for now in case of remote health provider
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE},targetEntity = HealthProvider.class,optional = true)
    @JoinColumn(referencedColumnName = "id", nullable = true)
    private HealthProvider healthProvider;
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE},targetEntity = User.class,optional = false)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE},targetEntity = AppointmentType.class, optional = false)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private AppointmentType appointmentType;

}
