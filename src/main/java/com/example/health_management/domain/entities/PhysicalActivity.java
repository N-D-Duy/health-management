package com.example.health_management.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "physical_activities")
public class PhysicalActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    ///todd: define measurement units for the following fields
    private Long duration;
    private Long caloriesBurned;
    private LocalDateTime activityDateTime;
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = Activity.class)
    @JoinTable(name = "activity_physical_activity",
            joinColumns = @JoinColumn(name = "physical_activity_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id",nullable = false))
    private Set<Activity> activity;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = User.class, optional = true)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private User user;

}
