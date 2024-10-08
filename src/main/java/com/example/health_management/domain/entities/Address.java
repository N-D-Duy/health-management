package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "addresses")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String unitNumber;
    private String streetNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String region;
    private String postalCode;
    private String country;
    private byte isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
