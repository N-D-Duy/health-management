package com.example.health_management.domain.entities;

import com.example.health_management.common.shared.enums.AppointmentStatus;
import com.example.health_management.common.shared.enums.AppointmentType;
import com.example.health_management.common.shared.enums.DepositStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @OneToOne(mappedBy = "appointmentRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,targetEntity = Doctor.class)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id",nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,targetEntity = HealthProvider.class)
    @JoinColumn(name = "health_provider_id", referencedColumnName = "id",nullable = false)
    private HealthProvider healthProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type")
    private AppointmentType appointmentType;

    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name="deposit_status")
    private DepositStatus depositStatus = DepositStatus.NONE;
}
