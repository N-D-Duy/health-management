package com.example.health_management.domain.entities;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payload implements Serializable {
    private int id;
    private String role;
    private int version;
    private String email;
}
