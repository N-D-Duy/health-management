package com.example.health_management.domain.repositories;

import com.example.health_management.common.utils.softdelete.SoftDeleteRepository;
import com.example.health_management.domain.entities.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends SoftDeleteRepository<Transaction, Long> {
    @Query("SELECT COUNT(t) > 0 FROM Transaction t WHERE t.zpTransToken = :zpTransToken")
    boolean existsByZPToken(String zpTransToken);

    @Query("SELECT t FROM Transaction t WHERE t.zpTransToken = :zpTransToken")
    Transaction findByZpTransToken(String zpTransToken);
}
