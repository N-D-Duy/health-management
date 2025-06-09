package com.example.health_management.domain.services;

import com.example.health_management.domain.entities.AppointmentRecord;
import com.example.health_management.domain.entities.Transaction;
import com.example.health_management.domain.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public void createTransaction(String transactionId, Long amount, AppointmentRecord appointmentRecord) {
        try {
            if (transactionRepository.existsByTransactionId(transactionId)) {
                throw new IllegalArgumentException("Transaction ID already exists");
            }
            Transaction transaction = new Transaction();
            transaction.setTransactionId(transactionId);
            transaction.setAmount(amount);
            transaction.setAppointmentRecord(appointmentRecord);
            transactionRepository.save(transaction);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isTransactionExists(String transactionId) {
        return transactionRepository.existsByTransactionId(transactionId);
    }

}
