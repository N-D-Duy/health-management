package com.example.health_management.domain.entities;


import com.example.health_management.common.shared.enums.DoctorSpecialization;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "doctors")
public class Doctor extends BaseEntity{
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DoctorSpecialization specialization;

    private Double experience;

    private String qualification;

    private Double rating;

    @Column(columnDefinition = "TEXT")
    private String about;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_provider_id", referencedColumnName = "id")
    private HealthProvider healthProvider;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorSchedule> schedules;

}
