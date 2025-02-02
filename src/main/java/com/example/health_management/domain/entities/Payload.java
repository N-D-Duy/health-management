package com.example.health_management.domain.entities;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payload implements Serializable {
    private Long id;
    private String role;
    private Integer version;
    private String email;
}
