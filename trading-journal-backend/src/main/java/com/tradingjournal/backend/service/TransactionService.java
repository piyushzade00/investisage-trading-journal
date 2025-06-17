package com.tradingjournal.backend.service;

import com.tradingjournal.backend.dto.transaction.TransactionRequestDTO;
import com.tradingjournal.backend.dto.transaction.TransactionResponseDTO;

import java.util.List;

public interface TransactionService {

    TransactionResponseDTO createTransaction(Long userId, TransactionRequestDTO request);

    List<TransactionResponseDTO> getAllTransactionsForAccount(Long accountId, Long userId);

    TransactionResponseDTO getTransactionByIdForAccountAndUser(Long transactionId, Long accountId, Long userId);

    TransactionResponseDTO updateTransaction(Long transactionId, Long accountId, Long userId, TransactionRequestDTO request);

    void deleteTransaction(Long transactionId, Long accountId, Long userId);
}
