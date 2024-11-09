package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_schedules")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DoctorSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Doctor.class)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer currentPatientCount;

    public Boolean isAvailable() {
        return currentPatientCount < doctor.getSpecialization().getMaxPatients();
    }
}
