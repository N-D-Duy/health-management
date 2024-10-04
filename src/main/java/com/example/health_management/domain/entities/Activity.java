package com.example.health_management.domain.entities;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "activities")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Activity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String activityName;
    private String activityDescription;
    private Double caloriesBurned;
}
