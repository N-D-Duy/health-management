package com.example.health_management.domain.entities;

import com.example.health_management.common.shared.enums.LoggingType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "logging")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Logging extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @Enumerated(EnumType.STRING)
    private LoggingType type;
}
