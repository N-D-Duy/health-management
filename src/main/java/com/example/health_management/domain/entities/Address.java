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
    ///todo: ask the meaning of unit number to rename this field if needed
    private String unitNumber;
    private String streetNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String region;
    private String postalCode;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,targetEntity = Country.class)
    @JoinColumn(referencedColumnName = "id",nullable = false)
    private Country country;

}
