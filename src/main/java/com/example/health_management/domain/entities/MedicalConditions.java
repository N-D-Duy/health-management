package com.example.health_management.domain.entities;

import com.example.health_management.common.shared.enums.MedicalConditionStatus;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "medical_conditions")
public class MedicalConditions extends BaseEntity{
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = Prescription.class)
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Prescription prescription;

    @Column(name = "condition_name")
    private String conditionName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MedicalConditionStatus status = MedicalConditionStatus.ACTIVE;

    @Column(name = "notes")
    private String notes;


}
