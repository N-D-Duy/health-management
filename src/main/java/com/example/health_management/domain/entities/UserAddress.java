package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_address")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,targetEntity = User.class)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL,targetEntity = Address.class)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private Address address;
    private boolean isDefault;
}
