package com.tradingjournal.backend.service.impl;

import com.tradingjournal.backend.dto.transaction.TransactionRequestDTO;
import com.tradingjournal.backend.dto.transaction.TransactionResponseDTO;
import com.tradingjournal.backend.entity.AccountEntity;
import com.tradingjournal.backend.entity.TransactionEntity;
import com.tradingjournal.backend.mapper.TransactionMapper;
import com.tradingjournal.backend.repository.AccountRepository;
import com.tradingjournal.backend.repository.TransactionRepository;
import com.tradingjournal.backend.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountRepository accountRepository,
                                  TransactionMapper transactionMapper){
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
    }

    // Create a new transaction for a specific account and user
    public TransactionResponseDTO createTransaction(Long userId, TransactionRequestDTO request) {
        // Verify that the account exists and belongs to the authenticated user
        AccountEntity account = accountRepository.findByIdAndUserId(request.getAccountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found or does not belong to user with ID: " + request.getAccountId()));

        TransactionEntity transaction = transactionMapper.toTransactionEntity(account,request);

        TransactionEntity savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.mapToDto(savedTransaction);
    }

    // Get all transactions for a specific account (and implicitly for the authenticated user)
    public List<TransactionResponseDTO> getAllTransactionsForAccount(Long accountId, Long userId) {
        // First, verify the account exists and belongs to the user
        AccountEntity account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found or does not belong to user with ID: " + accountId));

        List<TransactionEntity> transactions = transactionRepository.findByAccountId(accountId);
        return transactions.stream()
                .map(transactionMapper::mapToDto)
                .collect(Collectors.toList());
    }

    // Get a single transaction by ID, ensuring it belongs to the correct account and user
    public TransactionResponseDTO getTransactionByIdForAccountAndUser(Long transactionId, Long accountId, Long userId) {
        TransactionEntity transaction = transactionRepository.findByIdAndAccount_IdAndAccount_User_Id(transactionId, accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found or does not belong to the specified account/user."));
        return transactionMapper.mapToDto(transaction);
    }

    // Update an existing transaction, ensuring it belongs to the correct account and user
    public TransactionResponseDTO updateTransaction(Long transactionId, Long accountId, Long userId, TransactionRequestDTO request) {
        TransactionEntity existingTransaction = transactionRepository.findByIdAndAccount_IdAndAccount_User_Id(transactionId, accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found or does not belong to the specified account/user."));

        // Update fields from request
        existingTransaction = transactionMapper.toTransactionEntity(existingTransaction.getAccount(), request);

        TransactionEntity updatedTransaction = transactionRepository.save(existingTransaction);
        return transactionMapper.mapToDto(updatedTransaction);
    }

    // Delete a transaction, ensuring it belongs to the correct account and user
    public void deleteTransaction(Long transactionId, Long accountId, Long userId) {
        TransactionEntity transaction = transactionRepository.findByIdAndAccount_IdAndAccount_User_Id(transactionId, accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found or does not belong to the specified account/user."));
        transactionRepository.delete(transaction);
    }
}
