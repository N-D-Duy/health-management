package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users") // T
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = true, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = true, length = 50)
    private String lastName;

    @Column(name = "date_of_birth", nullable = true)
    private LocalDate dateOfBirth;

    @Column(name = "gender", nullable = true, length = 10)
    private String gender;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Account account;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Key key;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserAddress> userAddress;

}

