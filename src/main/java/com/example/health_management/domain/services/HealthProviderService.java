package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.heath_provider.HealthProviderDTO;
import com.example.health_management.application.DTOs.heath_provider.HealthProviderWithDoctorsDTO;
import com.example.health_management.application.mapper.HealthProviderMapper;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.HealthProvider;
import com.example.health_management.domain.repositories.DoctorRepository;
import com.example.health_management.domain.repositories.HealthProviderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthProviderService {
    private final HealthProviderRepository healthProviderRepository;
    private final DoctorRepository doctorRepository;
    private final HealthProviderMapper healthProviderMapper;

    @Transactional
    public HealthProviderDTO create(HealthProviderDTO request) {
        try{
            HealthProvider healthProvider = healthProviderMapper.toEntity(request);
            healthProvider = healthProviderRepository.save(healthProvider);
            return healthProviderMapper.toDTO(healthProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public HealthProviderWithDoctorsDTO update(HealthProviderDTO request) {
        try {
            HealthProvider healthProvider = healthProviderRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Health Provider not found"));
            healthProvider = healthProviderMapper.update(request, healthProvider);
            healthProvider = healthProviderRepository.save(healthProvider);
            return healthProviderMapper.toDTOWithDoctors(healthProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<HealthProviderWithDoctorsDTO> findAll() {
        try {
            List<HealthProvider> healthProviders = healthProviderRepository.findAllActive();
            return healthProviders.stream().map(healthProviderMapper::toDTOWithDoctors).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public HealthProviderWithDoctorsDTO doctorJoinHealthProvider(Long doctorId, Long healthProviderId) {
        try {
            // Verify both entities exist
            verifyEntitiesExist(doctorId, healthProviderId);

            // Execute the update
            int updatedRows = doctorRepository.joinHealthProvider(doctorId, healthProviderId);

            if (updatedRows == 0) {
                throw new ServiceException("Failed to update doctor's health provider");
            }

            return fetchUpdatedHealthProvider(healthProviderId);
        } catch (Exception e) {
            log.error("Failed to add doctor to health provider", e);
            throw new ServiceException("Failed to add doctor to health provider", e);
        }
    }

    @Transactional
    public HealthProviderWithDoctorsDTO doctorLeaveHealthProvider(Long doctorId, Long healthProviderId) {
        try {
            // Verify relationship exists
            if (!doctorRepository.isDoctorInHealthProvider(doctorId, healthProviderId)) {
                throw new IllegalStateException("Doctor is not part of this Health Provider");
            }

            // Execute the update
            int updatedRows = doctorRepository.leaveHealthProvider(doctorId, healthProviderId);

            if (updatedRows == 0) {
                throw new ServiceException("Failed to remove doctor from health provider");
            }

            return fetchUpdatedHealthProvider(healthProviderId);
        } catch (Exception e) {
            log.error("Failed to remove doctor from health provider", e);
            throw new ServiceException("Failed to remove doctor from health provider", e);
        }
    }

    private void verifyEntitiesExist(Long doctorId, Long healthProviderId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new EntityNotFoundException("Doctor not found");
        }
        if (!healthProviderRepository.existsById(healthProviderId)) {
            throw new EntityNotFoundException("Health Provider not found");
        }
    }

    private HealthProviderWithDoctorsDTO fetchUpdatedHealthProvider(Long healthProviderId) {
        HealthProvider updatedHealthProvider = healthProviderRepository.findByIdWithDoctors(healthProviderId)
                .orElseThrow(() -> new EntityNotFoundException("Health Provider not found"));
        return healthProviderMapper.toDTOWithDoctors(updatedHealthProvider);
    }

}
