package com.tradingjournal.backend.repository;

import com.tradingjournal.backend.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    // Find all accounts belonging to a specific user
    List<AccountEntity> findByUserId(Long userId);

    // Find a specific account by ID and ensure it belongs to a specific user
    Optional<AccountEntity> findByIdAndUserId(Long id, Long userId);
}
