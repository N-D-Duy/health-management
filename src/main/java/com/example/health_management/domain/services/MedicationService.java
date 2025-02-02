package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.medication.MedicationDTO;
import com.example.health_management.application.mapper.MedicationMapper;
import com.example.health_management.domain.repositories.MedicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MedicationService {
    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;

    public List<MedicationDTO> findAll() {
        return medicationRepository.findAll().stream().map(medicationMapper::toDTO).toList();
    }

    public MedicationDTO getById(Long medicationId) {
        return medicationMapper.toDTO(medicationRepository.findById(medicationId).orElseThrow());
    }

    public MedicationDTO create(MedicationDTO medicationDTO) {
        return medicationMapper.toDTO(medicationRepository.save(medicationMapper.toEntity(medicationDTO)));
    }

    public MedicationDTO update(MedicationDTO medicationDTO) {
        return medicationMapper.toDTO(medicationRepository.save(medicationMapper.updateFromDTO(medicationDTO, medicationRepository.findById(medicationDTO.getId()).orElseThrow())));
    }
}
