package com.tradingjournal.backend.service.impl;

import com.tradingjournal.backend.dto.account.AccountRequestDTO;
import com.tradingjournal.backend.dto.account.AccountResponseDTO;
import com.tradingjournal.backend.entity.AccountEntity;
import com.tradingjournal.backend.entity.TransactionEntity;
import com.tradingjournal.backend.entity.UserEntity;
import com.tradingjournal.backend.mapper.AccountMapper;
import com.tradingjournal.backend.mapper.TransactionMapper;
import com.tradingjournal.backend.repository.AccountRepository;
import com.tradingjournal.backend.repository.UserRepository;
import com.tradingjournal.backend.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              UserRepository userRepository,
                              AccountMapper accountMapper,
                              TransactionMapper transactionMapper){
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountMapper = accountMapper;
        this.transactionMapper = transactionMapper;
    }

    // Create a new account for a user
    @Override
    @Transactional
    public AccountResponseDTO createAccount(Long userId, AccountRequestDTO request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        accountRepository.findByNameAndUserId(request.getName(), userId)
                .ifPresent(existingAccount -> {
                    throw new IllegalArgumentException("Account with name '" + request.getName() + "' already exists for this user.");
                });

        // Business logic for isPrimary: Ensuring only one primary account per user
        if (request.isPrimary()) {
            accountRepository.findByUserIdAndIsPrimary(userId, true)
                    .ifPresent(currentPrimary -> {
                        currentPrimary.setPrimary(false);
                        accountRepository.save(currentPrimary);
                    });
        } else {
            // If the new account is NOT set as primary, check if this is the user's FIRST account.
            // If it's the first account, it should automatically be set as primary.
            if (accountRepository.countByUserId(userId) == 0) {
                request.setPrimary(true); // Automatically make the first account primary
            }
        }

        AccountEntity account = accountMapper.toAccountEntity(user, request);
        account.setTransactions(null);
        AccountEntity savedAccount = accountRepository.save(account);

        if (request.getTransactions() != null && !request.getTransactions().isEmpty()) {
            List<TransactionEntity> transactions = transactionMapper.toTransactionEntity(savedAccount, request.getTransactions());
            validateNoOverdraw(transactions);
            savedAccount.setTransactions(transactions);
            accountRepository.save(savedAccount);
        }

        return accountMapper.mapToDto(savedAccount);
    }

    // Get all accounts for a specific user
    @Override
    public List<AccountResponseDTO> getAllAccountsForUser(Long userId) {
        List<AccountEntity> accounts = accountRepository.findByUserId(userId);
        return accounts.stream()
                .map(accountMapper::mapToDto)
                .collect(Collectors.toList());
    }

    // Get a single account by ID, ensuring it belongs to the user
    @Override
    public AccountResponseDTO getAccountByIdForUser(Long accountId, Long userId) {
        AccountEntity account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found or does not belong to user with ID: " + accountId));

        return accountMapper.mapToDto(account);
    }

    // Update an existing account, ensuring it belongs to the user
    @Override
    @Transactional
    public AccountResponseDTO updateAccount(Long accountId, Long userId, AccountRequestDTO request) {
        AccountEntity existingAccount = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found or does not belong to user with ID: " + accountId));

        existingAccount.setName(request.getName());
        existingAccount.setBroker(request.getBroker());

        // Business logic for isPrimary:
        if (request.isPrimary() && !existingAccount.isPrimary()) {
            // If the request wants to set this account as primary AND it wasn't primary before
            // Find the current primary for this user and set it to false
            accountRepository.findByUserIdAndIsPrimary(userId, true)
                    .ifPresent(currentPrimary -> {
                        // Ensure we're not unsetting the same account if it was already primary
                        if (!currentPrimary.getId().equals(accountId)) {
                            currentPrimary.setPrimary(false);
                            accountRepository.save(currentPrimary); // Save the updated old primary
                        }
                    });
            existingAccount.setPrimary(true); // Set the requested account as primary
        } else if (!request.isPrimary() && existingAccount.isPrimary()) {
            existingAccount.setPrimary(false);
        }

        // Remove all existing transactions and set new ones
        existingAccount.getTransactions().clear();
        if (request.getTransactions() != null && !request.getTransactions().isEmpty()) {
            List<TransactionEntity> newTransactions = request.getTransactions().stream()
                    .map(dto -> TransactionEntity.builder()
                            .account(existingAccount)
                            .type(dto.getType())
                            .amount(dto.getAmount())
                            .transactionDateTime(dto.getTransactionDateTime())
                            .notes(dto.getNotes())
                            .build())
                    .collect(Collectors.toList());
            existingAccount.getTransactions().addAll(newTransactions);
        }

        AccountEntity updatedAccount = accountRepository.save(existingAccount);
        return accountMapper.mapToDto(updatedAccount);
    }

    // Delete an account, ensuring it belongs to the user
    @Override
    public void deleteAccount(Long accountId, Long userId) {
        AccountEntity account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found or does not belong to user with ID: " + accountId));

        // Before deleting a primary account
        if (account.isPrimary()) {
            // Find another account for this user and set it as primary
            accountRepository.findTopByUserIdAndIdNot(userId, accountId)
                    .ifPresent(newPrimary -> {
                        newPrimary.setPrimary(true);
                        accountRepository.save(newPrimary);
                    });
        }

       accountRepository.delete(account);
    }

    private void validateNoOverdraw(List<TransactionEntity> transactions) {
        BigDecimal balance = BigDecimal.ZERO;
        for (TransactionEntity tx : transactions) {
            if (tx.getType() == com.tradingjournal.backend.enums.TransactionType.DEPOSIT) {
                balance = balance.add(tx.getAmount());
            } else if (tx.getType() == com.tradingjournal.backend.enums.TransactionType.WITHDRAWAL) {
                if (balance.compareTo(tx.getAmount()) < 0) {
                    throw new IllegalArgumentException("Withdrawal of " + tx.getAmount() + " exceeds available balance: " + balance);
                }
                balance = balance.subtract(tx.getAmount());
            }
        }
    }
}
