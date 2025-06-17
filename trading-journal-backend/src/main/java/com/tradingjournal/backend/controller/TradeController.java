package com.tradingjournal.backend.controller;

import com.tradingjournal.backend.dto.trade.TradeRequestDTO;
import com.tradingjournal.backend.dto.trade.TradeResponseDTO;
import com.tradingjournal.backend.entity.UserEntity;
import com.tradingjournal.backend.service.TradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/{accountId}/trades")
public class TradeController {

    private final TradeService tradeService;

    @Autowired
    public TradeController(TradeService tradeService){
        this.tradeService = tradeService;
    }

    // Create a new trade for a specific account
    @PostMapping
    public ResponseEntity<TradeResponseDTO> createTrade(
            @PathVariable Long accountId,
            @Valid @RequestBody TradeRequestDTO request,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        // Ensure the request's accountId matches the path variable for consistency
        if (!accountId.equals(request.getAccountId())) {
            return ResponseEntity.badRequest().body(TradeResponseDTO.builder().notes("Account ID in path must match account ID in request body.").build());
        }
        TradeResponseDTO response = tradeService.createTrade(currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all trades for a specific account
    @GetMapping
    public ResponseEntity<List<TradeResponseDTO>> getAllTradesForAccount(
            @PathVariable Long accountId,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        List<TradeResponseDTO> trades = tradeService.getAllTradesForAccount(accountId, currentUser.getId());
        return ResponseEntity.ok(trades);
    }

    // Get a specific trade by ID within a specific account
    @GetMapping("/{tradeId}")
    public ResponseEntity<TradeResponseDTO> getTradeById(
            @PathVariable Long accountId,
            @PathVariable Long tradeId,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        TradeResponseDTO trade = tradeService.getTradeByIdForAccountAndUser(tradeId, accountId, currentUser.getId());
        return ResponseEntity.ok(trade);
    }

    // Update an existing trade within a specific account
    @PutMapping("/{tradeId}")
    public ResponseEntity<TradeResponseDTO> updateTrade(
            @PathVariable Long accountId,
            @PathVariable Long tradeId,
            @Valid @RequestBody TradeRequestDTO request,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        // Ensure the request's accountId matches the path variable for consistency
        if (!accountId.equals(request.getAccountId())) {
            return ResponseEntity.badRequest().body(TradeResponseDTO.builder().notes("Account ID in path must match account ID in request body.").build());
        }
        TradeResponseDTO updatedTrade = tradeService.updateTrade(tradeId, accountId, currentUser.getId(), request);
        return ResponseEntity.ok(updatedTrade);
    }

    // Delete a trade within a specific account
    @DeleteMapping("/{tradeId}")
    public ResponseEntity<Void> deleteTrade(
            @PathVariable Long accountId,
            @PathVariable Long tradeId,
            @AuthenticationPrincipal UserEntity currentUser
    ) {
        tradeService.deleteTrade(tradeId, accountId, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
