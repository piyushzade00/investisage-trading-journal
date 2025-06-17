package com.tradingjournal.backend.service;

import com.tradingjournal.backend.dto.trade.TradeRequestDTO;
import com.tradingjournal.backend.dto.trade.TradeResponseDTO;

import java.util.List;

public interface TradeService {

    TradeResponseDTO createTrade(Long userId, TradeRequestDTO request);

    List<TradeResponseDTO> getAllTradesForAccount(Long accountId, Long userId);

    TradeResponseDTO getTradeByIdForAccountAndUser(Long tradeId, Long accountId, Long userId);

    TradeResponseDTO updateTrade(Long tradeId, Long accountId, Long userId, TradeRequestDTO request);

    void deleteTrade(Long tradeId, Long accountId, Long userId);
}
