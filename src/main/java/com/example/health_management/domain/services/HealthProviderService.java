package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.heath_provider.HealthProviderDTO;
import com.example.health_management.application.DTOs.heath_provider.HealthProviderWithDoctorsDTO;
import com.example.health_management.application.mapper.DoctorMapper;
import com.example.health_management.application.mapper.HealthProviderMapper;
import com.example.health_management.common.shared.enums.DoctorSpecialization;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.entities.Doctor;
import com.example.health_management.domain.entities.DoctorSchedule;
import com.example.health_management.domain.entities.HealthProvider;
import com.example.health_management.domain.repositories.DoctorRepository;
import com.example.health_management.domain.repositories.HealthProviderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HealthProviderService {
    private final HealthProviderRepository healthProviderRepository;
    private final DoctorRepository doctorRepository;
    private final HealthProviderMapper healthProviderMapper;
    private final DoctorMapper doctorMapper;

    public HealthProviderDTO create(HealthProviderDTO request) {
        try{
            HealthProvider healthProvider = healthProviderMapper.toEntity(request);
            healthProvider = healthProviderRepository.save(healthProvider);
            return healthProviderMapper.toDTO(healthProvider);
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public HealthProviderWithDoctorsDTO update(HealthProviderDTO request) {
        try {
            HealthProvider healthProvider = healthProviderRepository.findById(request.getId())
                    .orElseThrow(() -> new ConflictException("Health Provider not found"));
            healthProvider = healthProviderMapper.update(request, healthProvider);
            healthProvider = healthProviderRepository.save(healthProvider);
            return healthProviderMapper.toDTOWithDoctors(healthProvider);
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    public List<HealthProviderWithDoctorsDTO> findAll() {
        try {
            List<HealthProvider> healthProviders = healthProviderRepository.findAllActive();
            return healthProviders.stream().map(healthProviderMapper::toDTOWithDoctors).toList();
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

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

//    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<HealthProviderWithDoctorsDTO> getDoctorsAvailableForTimes(String specialization, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            DoctorSpecialization spec = DoctorSpecialization.valueOf(specialization);
            List<HealthProvider> healthProviders = healthProviderRepository.findAllWithDoctorsBySpecialization(spec);

            List<Doctor> allDoctors = healthProviders.stream()
                    .flatMap(hp -> hp.getDoctors().stream())
                    .toList();

            List<Doctor> doctorsWithSchedules = doctorRepository.findDoctorsWithSchedules(allDoctors);

            Map<Long, Doctor> doctorMap = doctorsWithSchedules.stream()
                    .collect(Collectors.toMap(Doctor::getId, d -> d));

            return healthProviders.stream()
                    .map(hp -> {
                        List<Doctor> availableDoctors = hp.getDoctors().stream()
                                .map(d -> doctorMap.get(d.getId()))
                                .filter(doctor -> isAvailableForTimeSlot(doctor, startTime, endTime))
                                .toList();

                        if (!availableDoctors.isEmpty()) {
                            HealthProviderWithDoctorsDTO dto = healthProviderMapper.toDTOWithDoctors(hp);
                            dto.setDoctors(availableDoctors.stream().map(doctorMapper::toSummary).toList());
                            return dto;
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .toList();
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    private boolean isAvailableForTimeSlot(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime) {
        List<DoctorSchedule> overlappingSchedules = doctor.getSchedules().stream()
                .filter(schedule ->
                        !(schedule.getEndTime().isBefore(startTime) || schedule.getStartTime().isAfter(endTime)))
                .toList();

        if (overlappingSchedules.isEmpty()) {
            // No overlapping schedules means doctor is available
            return true;
        }

        return overlappingSchedules.stream()
                .allMatch(DoctorSchedule::isAvailable);
    }

}
