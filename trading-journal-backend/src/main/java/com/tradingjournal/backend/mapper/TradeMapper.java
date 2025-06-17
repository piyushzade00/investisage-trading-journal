package com.tradingjournal.backend.mapper;

import com.tradingjournal.backend.dto.trade.TradeRequestDTO;
import com.tradingjournal.backend.dto.trade.TradeResponseDTO;
import com.tradingjournal.backend.entity.AccountEntity;
import com.tradingjournal.backend.entity.TradeEntity;
import org.springframework.stereotype.Component;

@Component
public class TradeMapper {

    public TradeResponseDTO mapToDto(TradeEntity trade) {
        return TradeResponseDTO.builder()
                .id(trade.getId())
                .accountId(trade.getAccount().getId())
                .instrument(trade.getInstrument())
                .type(trade.getType())
                .quantity(trade.getQuantity())
                .entryPrice(trade.getEntryPrice())
                .exitPrice(trade.getExitPrice())
                .entryDateTime(trade.getEntryDateTime())
                .exitDateTime(trade.getExitDateTime())
                .stopLoss(trade.getStopLoss())
                .targetPrice(trade.getTargetPrice())
                .profitLoss(trade.getProfitLoss())
                .notes(trade.getNotes())
                .build();
    }

    public TradeEntity toTradeEntity(AccountEntity account, TradeRequestDTO request){
        TradeEntity trade = TradeEntity.builder()
                .account(account)
                .instrument(request.getInstrument())
                .type(request.getType())
                .quantity(request.getQuantity())
                .entryPrice(request.getEntryPrice())
                .exitPrice(request.getExitPrice())
                .entryDateTime(request.getEntryDateTime())
                .exitDateTime(request.getExitDateTime())
                .stopLoss(request.getStopLoss())
                .targetPrice(request.getTargetPrice())
                .notes(request.getNotes())
                .build();

        return trade;
    }
}
