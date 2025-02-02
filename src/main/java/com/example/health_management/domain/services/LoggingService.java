package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.logging.LoggingDTO;
import com.example.health_management.application.mapper.LoggingMapper;
import com.example.health_management.common.shared.enums.LoggingType;
import com.example.health_management.common.shared.exceptions.ConflictException;
import com.example.health_management.domain.entities.Logging;
import com.example.health_management.domain.repositories.LoggingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LoggingService {
    private final LoggingRepository loggingRepository;
    private final LoggingMapper loggingMapper;

    public void saveLog(LoggingDTO loggingDTO) {
        try {
            Logging logging = loggingMapper.toEntity(loggingDTO);
            log.info("Logging: " + logging.getMessage());
            loggingRepository.save(logging);
        } catch (Exception e) {
            throw new ConflictException("Error saving log");
        }
    }

    public List<Logging> getLogs() {
        try {

            return loggingRepository.findAll();
        } catch (Exception e) {
            throw new ConflictException("Error getting logs");
        }
    }

    public List<Logging> getLogsByType(LoggingType type) {
        try{
        return loggingRepository.findByType(type);
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }
}
