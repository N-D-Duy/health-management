package com.example.health_management.domain.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;

@Setter
@Getter
@MappedSuperclass
public class UserBaseInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @OneToOne(mappedBy = "userBasedInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Account account;

    @OneToOne(mappedBy = "userBaseInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Key key;
}
