package com.tradingjournal.backend.repository;

import com.tradingjournal.backend.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<TradeEntity, Long> {
    // Find all trades for a specific account
    List<TradeEntity> findByAccountId(Long accountId);

    // Find a specific trade by ID and ensure it belongs to a specific account AND user
    // This method combines account and user verification for security and correctness
    Optional<TradeEntity> findByIdAndAccount_IdAndAccount_User_Id(Long tradeId, Long accountId, Long userId);
}