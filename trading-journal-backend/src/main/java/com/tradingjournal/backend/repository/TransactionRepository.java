package com.tradingjournal.backend.repository;

import com.tradingjournal.backend.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository  extends JpaRepository<TransactionEntity, Long> {
    // Find all transactions for a specific account
    List<TransactionEntity> findByAccountId(Long accountId);

    List<TransactionEntity> findByAccount_IdAndAccount_User_Id(Long accountId, Long userId);

    Optional<TransactionEntity> findByIdAndAccount_IdAndAccount_User_Id(Long transactionId, Long accountId, Long userId);
}