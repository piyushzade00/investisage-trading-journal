package com.tradingjournal.backend.service.impl;

import com.tradingjournal.backend.dto.trade.TradeRequestDTO;
import com.tradingjournal.backend.dto.trade.TradeResponseDTO;
import com.tradingjournal.backend.entity.AccountEntity;
import com.tradingjournal.backend.entity.TradeEntity;
import com.tradingjournal.backend.enums.TradeType;
import com.tradingjournal.backend.mapper.TradeMapper;
import com.tradingjournal.backend.repository.AccountRepository;
import com.tradingjournal.backend.repository.TradeRepository;
import com.tradingjournal.backend.service.TradeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;
    private final AccountRepository accountRepository;
    private final TradeMapper tradeMapper;

    public TradeServiceImpl(TradeRepository tradeRepository,
                            AccountRepository accountRepository,
                            TradeMapper tradeMapper){
        this.tradeRepository = tradeRepository;
        this.accountRepository = accountRepository;
        this.tradeMapper = tradeMapper;
    }

    // Helper method to calculate P&L
    private BigDecimal calculateProfitLoss(TradeEntity trade) {
        if (trade.getExitPrice() == null || trade.getEntryPrice() == null) {
            return BigDecimal.ZERO; // P&L is 0 or not calculable if trade is open
        }

        BigDecimal pnlPerUnit;
        if (trade.getType() == TradeType.BUY) {
            pnlPerUnit = trade.getExitPrice().subtract(trade.getEntryPrice());
        } else { // SELL (Short)
            pnlPerUnit = trade.getEntryPrice().subtract(trade.getExitPrice());
        }

        // Multiply by quantity and round to 2 decimal places
        return pnlPerUnit.multiply(BigDecimal.valueOf(trade.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    // Create a new trade for a specific account and user
    public TradeResponseDTO createTrade(Long userId, TradeRequestDTO request) {
        // Verify that the account exists and belongs to the authenticated user
        AccountEntity account = accountRepository.findByIdAndUserId(request.getAccountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found or does not belong to user with ID: " + request.getAccountId()));

        TradeEntity trade = tradeMapper.toTradeEntity(account,request);

        // Calculate initial P&L (if exit price is provided)
        trade.setProfitLoss(calculateProfitLoss(trade));

        TradeEntity savedTrade = tradeRepository.save(trade);
        return tradeMapper.mapToDto(savedTrade);
    }

    // Get all trades for a specific account (and implicitly for the authenticated user)
    public List<TradeResponseDTO> getAllTradesForAccount(Long accountId, Long userId) {
        // First, verify the account exists and belongs to the user
        AccountEntity account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found or does not belong to user with ID: " + accountId));

        List<TradeEntity> trades = tradeRepository.findByAccountId(accountId);
        return trades.stream()
                .map(tradeMapper::mapToDto)
                .collect(Collectors.toList());
    }

    // Get a single trade by ID, ensuring it belongs to the correct account and user
    public TradeResponseDTO getTradeByIdForAccountAndUser(Long tradeId, Long accountId, Long userId) {
        TradeEntity trade = tradeRepository.findByIdAndAccount_IdAndAccount_User_Id(tradeId, accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Trade not found or does not belong to the specified account/user."));
        return tradeMapper.mapToDto(trade);
    }

    // Update an existing trade, ensuring it belongs to the correct account and user
    public TradeResponseDTO updateTrade(Long tradeId, Long accountId, Long userId, TradeRequestDTO request) {
        TradeEntity existingTrade = tradeRepository.findByIdAndAccount_IdAndAccount_User_Id(tradeId, accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Trade not found or does not belong to the specified account/user."));

        existingTrade = tradeMapper.toTradeEntity(existingTrade.getAccount(),request);

        // Recalculate P&L after update
        existingTrade.setProfitLoss(calculateProfitLoss(existingTrade));

        TradeEntity updatedTrade = tradeRepository.save(existingTrade);
        return tradeMapper.mapToDto(updatedTrade);
    }

    // Delete a trade, ensuring it belongs to the correct account and user
    public void deleteTrade(Long tradeId, Long accountId, Long userId) {
        TradeEntity trade = tradeRepository.findByIdAndAccount_IdAndAccount_User_Id(tradeId, accountId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Trade not found or does not belong to the specified account/user."));
        tradeRepository.delete(trade);
    }
}
