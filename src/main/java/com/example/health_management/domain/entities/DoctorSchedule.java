package com.example.health_management.domain.entities;

import com.example.health_management.common.shared.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private String patientName;
    private String examinationType;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;
    private String note;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = AppointmentRecord.class)
    @JoinColumn(name = "appointment_record_id", referencedColumnName = "id")
    private AppointmentRecord appointmentRecord;
}
