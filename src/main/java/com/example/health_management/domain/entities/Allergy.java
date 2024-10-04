package com.example.health_management.domain.entities;

import com.example.health_management.common.shared.enums.AllergyType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "allergies")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Allergy extends BaseEntity{
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "allergy_name")
    private AllergyType allergyType;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = User.class)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private User user;

    @Column(name = "severity")
    private String severity;

    @Column(name = "note")
    private String note;
}
