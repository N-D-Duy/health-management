package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.MedicalConditions;

public interface MedicalConditionRepository extends SoftDeleteRepository<MedicalConditions, Long> {
}
