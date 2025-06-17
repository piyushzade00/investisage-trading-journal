package com.tradingjournal.backend.service.impl;

import com.tradingjournal.backend.dto.account.AccountRequestDTO;
import com.tradingjournal.backend.dto.account.AccountResponseDTO;
import com.tradingjournal.backend.entity.AccountEntity;
import com.tradingjournal.backend.entity.UserEntity;
import com.tradingjournal.backend.mapper.AccountMapper;
import com.tradingjournal.backend.repository.AccountRepository;
import com.tradingjournal.backend.repository.UserRepository;
import com.tradingjournal.backend.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              UserRepository userRepository,
                              AccountMapper accountMapper){
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountMapper = accountMapper;
    }

    // Create a new account for a user
    public AccountResponseDTO createAccount(Long userId, AccountRequestDTO request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        AccountEntity account = accountMapper.toAccountEntity(user,request);

        AccountEntity savedAccount = accountRepository.save(account);

        return accountMapper.mapToDto(savedAccount);
    }

    // Get all accounts for a specific user
    public List<AccountResponseDTO> getAllAccountsForUser(Long userId) {
        List<AccountEntity> accounts = accountRepository.findByUserId(userId);
        return accounts.stream()
                .map(accountMapper::mapToDto)
                .collect(Collectors.toList());
    }

    // Get a single account by ID, ensuring it belongs to the user
    public AccountResponseDTO getAccountByIdForUser(Long accountId, Long userId) {
        AccountEntity account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found or does not belong to user with ID: " + accountId));
        return accountMapper.mapToDto(account);
    }

    // Update an existing account, ensuring it belongs to the user
    public AccountResponseDTO updateAccount(Long accountId, Long userId, AccountRequestDTO request) {
        AccountEntity existingAccount = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found or does not belong to user with ID: " + accountId));

        existingAccount.setName(request.getName());
        existingAccount.setBroker(request.getBroker());

        AccountEntity updatedAccount = accountRepository.save(existingAccount);
        return accountMapper.mapToDto(updatedAccount);
    }

    // Delete an account, ensuring it belongs to the user
    public void deleteAccount(Long accountId, Long userId) {
        AccountEntity account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found or does not belong to user with ID: " + accountId));

       accountRepository.delete(account);
    }
}
