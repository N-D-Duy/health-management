package com.example.health_management.infrastructure.crons.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobProfile {
    private String name;
    private String cron;
}
