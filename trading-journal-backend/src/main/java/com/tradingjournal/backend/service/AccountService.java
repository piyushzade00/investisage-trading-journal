package com.tradingjournal.backend.service;

import com.tradingjournal.backend.dto.account.AccountRequestDTO;
import com.tradingjournal.backend.dto.account.AccountResponseDTO;

import java.util.List;

public interface AccountService {

    AccountResponseDTO createAccount(Long userId, AccountRequestDTO request);

    List<AccountResponseDTO> getAllAccountsForUser(Long userId);

    AccountResponseDTO getAccountByIdForUser(Long accountId, Long userId);

    AccountResponseDTO updateAccount(Long accountId, Long userId, AccountRequestDTO request);

    void deleteAccount(Long accountId, Long userId);
}
