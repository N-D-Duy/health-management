package com.example.health_management.domain.entities;

import lombok.*;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payload {
    private int id;
    private String role;
    private String email;
}
