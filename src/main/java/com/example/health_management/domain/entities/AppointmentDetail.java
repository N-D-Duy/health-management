package com.example.health_management.domain.entities;

import com.example.health_management.common.shared.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appointment_details")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppointmentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private Status detailName;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = Appointment.class)
    private Appointment appointment;
}
