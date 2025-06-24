package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.payment.UpdateTransRequest;
import com.example.health_management.domain.entities.AppointmentRecord;
import com.example.health_management.domain.entities.Transaction;
import com.example.health_management.domain.repositories.AppointmentRecordRepository;
import com.example.health_management.domain.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AppointmentRecordRepository appointmentRecordRepository;

    public void createTransaction(String zpTransToken, Long amount) {
        try {
            if (transactionRepository.existsByZPToken(zpTransToken)) {
                throw new IllegalArgumentException("Transaction ID already exists");
            }
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setZpTransToken(zpTransToken);
            transactionRepository.save(transaction);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTransaction(UpdateTransRequest req) {
        String zpTokens = req.getZpTokens();
        Long appointmentId = req.getAppointmentId();
        String transactionId = req.getTransactionId();
        if (zpTokens == null || zpTokens.isEmpty()) {
            throw new IllegalArgumentException("ZP Tokens cannot be null or empty");
        }
        if (transactionId == null || transactionId.isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
        }
        if (appointmentId == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }
        AppointmentRecord appointmentRecord = appointmentRecordRepository.findById(appointmentId).orElse(null);
        if (appointmentRecord == null) {
            throw new RuntimeException("Appointment Record not found");
        }
        Transaction transaction = transactionRepository.findByZpTransToken(zpTokens);
        if(transaction == null) {
            throw new RuntimeException("Transaction not found");
        }
        transaction.setTransactionId(transactionId);
        transaction.setAppointmentRecord(appointmentRecord);
        transactionRepository.save(transaction);
    }

    public boolean isTransactionExists(String transactionId) {
        return transactionRepository.existsByZPToken(transactionId);
    }

}
