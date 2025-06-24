package com.example.health_management.infrastructure.crons.refund.service;

import com.example.health_management.domain.repositories.AppointmentRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefundService {
    private final AppointmentRecordRepository appointmentRecordRepository;
    private int count = 0;

    public void processRefunds() {
        log.info("Processing refunds for appointments... Count: {}", count++);
        log.info("Current time: {}", java.time.LocalDateTime.now());
        
        // TODO: Implement actual refund logic
        // For now, just log that the service is working
        log.info("Refund service is working correctly!");
    }
}
