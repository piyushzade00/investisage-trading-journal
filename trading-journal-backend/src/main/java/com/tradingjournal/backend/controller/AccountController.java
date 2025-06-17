package com.tradingjournal.backend.controller;

import com.tradingjournal.backend.dto.account.AccountRequestDTO;
import com.tradingjournal.backend.dto.account.AccountResponseDTO;
import com.tradingjournal.backend.entity.UserEntity;
import com.tradingjournal.backend.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    // Create a new account
    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(
            @Valid @RequestBody AccountRequestDTO request,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        AccountResponseDTO response = accountService.createAccount(currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all accounts for the authenticated user
    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts(
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        List<AccountResponseDTO> accounts = accountService.getAllAccountsForUser(currentUser.getId());
        return ResponseEntity.ok(accounts);
    }

    // Get a specific account by ID for the authenticated user
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> getAccountById(
            @PathVariable Long accountId,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        AccountResponseDTO account = accountService.getAccountByIdForUser(accountId, currentUser.getId());
        return ResponseEntity.ok(account);
    }

    // Update an existing account for the authenticated user
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> updateAccount(
            @PathVariable Long accountId,
            @Valid @RequestBody AccountRequestDTO request,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        AccountResponseDTO updatedAccount = accountService.updateAccount(accountId, currentUser.getId(), request);
        return ResponseEntity.ok(updatedAccount);
    }

    // Delete an account for the authenticated user
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable Long accountId,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        accountService.deleteAccount(accountId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
