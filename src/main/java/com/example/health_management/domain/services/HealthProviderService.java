package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.heath_provider.HealthProviderDTO;
import com.example.health_management.application.DTOs.heath_provider.HealthProviderRequest;
import com.example.health_management.application.mapper.HealthProviderMapper;
import com.example.health_management.domain.entities.HealthProvider;
import com.example.health_management.domain.repositories.HealthProviderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthProviderService {
    private final HealthProviderRepository healthProviderRepository;
    private final HealthProviderMapper healthProviderMapper;

    @Transactional
    public HealthProviderDTO create(HealthProviderRequest request) {
        try{
            HealthProvider healthProvider = healthProviderMapper.toEntity(request);
            healthProvider = healthProviderRepository.save(healthProvider);
            return healthProviderMapper.toDTO(healthProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public HealthProviderDTO update(Long id, HealthProviderRequest request) {
        try {
            HealthProvider healthProvider = healthProviderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Health Provider not found"));
            healthProvider = healthProviderMapper.update(request, healthProvider);
            healthProvider = healthProviderRepository.save(healthProvider);
            return healthProviderMapper.toDTO(healthProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<HealthProviderDTO> findAll() {
        try {
            List<HealthProvider> healthProviders = healthProviderRepository.findAllActive();
            return healthProviders.stream().map(healthProviderMapper::toDTO).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
