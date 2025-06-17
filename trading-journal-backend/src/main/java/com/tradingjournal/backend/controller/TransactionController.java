package com.tradingjournal.backend.controller;
import com.tradingjournal.backend.dto.transaction.TransactionRequestDTO;
import com.tradingjournal.backend.dto.transaction.TransactionResponseDTO;
import com.tradingjournal.backend.entity.UserEntity;
import com.tradingjournal.backend.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/{accountId}/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    // Create a new transaction for a specific account
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionRequestDTO request,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        // Ensure the request's accountId matches the path variable for consistency
        if (!accountId.equals(request.getAccountId())) {
            return ResponseEntity.badRequest().body(TransactionResponseDTO.builder().notes("Account ID in path must match account ID in request body.").build());
        }
        TransactionResponseDTO response = transactionService.createTransaction(currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all transactions for a specific account
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsForAccount(
            @PathVariable Long accountId,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        List<TransactionResponseDTO> transactions = transactionService.getAllTransactionsForAccount(accountId, currentUser.getId());
        return ResponseEntity.ok(transactions);
    }

    // Get a specific transaction by ID within a specific account
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(
            @PathVariable Long accountId,
            @PathVariable Long transactionId,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        TransactionResponseDTO transaction = transactionService.getTransactionByIdForAccountAndUser(transactionId, accountId, currentUser.getId());
        return ResponseEntity.ok(transaction);
    }

    // Update an existing transaction within a specific account
    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long accountId,
            @PathVariable Long transactionId,
            @Valid @RequestBody TransactionRequestDTO request,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        // Ensure the request's accountId matches the path variable for consistency
        if (!accountId.equals(request.getAccountId())) {
            return ResponseEntity.badRequest().body(TransactionResponseDTO.builder().notes("Account ID in path must match account ID in request body.").build());
        }
        TransactionResponseDTO updatedTransaction = transactionService.updateTransaction(transactionId, accountId, currentUser.getId(), request);
        return ResponseEntity.ok(updatedTransaction);
    }

    // Delete a transaction within a specific account
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long accountId,
            @PathVariable Long transactionId,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        transactionService.deleteTransaction(transactionId, accountId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
