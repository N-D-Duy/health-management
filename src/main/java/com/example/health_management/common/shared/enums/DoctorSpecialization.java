package com.example.health_management.common.shared.enums;

import lombok.Getter;

@Getter
public enum DoctorSpecialization {
    NEUROLOGIST(2),
    PEDIATRICIAN(2),
    DIETICIAN(3),
    PSYCHOLOGIST(1);

    private final int maxPatients;

    DoctorSpecialization(int maxPatients) {
        this.maxPatients = maxPatients;
    }

}
