package com.example.health_management.domain.entities;

import com.example.health_management.common.shared.enums.HealthPlan;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "user_health_plans")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserHealthPlan {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type")
    private HealthPlan planType;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "goals")
    private String goals;

    @Column(name = "notes")
    private String notes;
}
