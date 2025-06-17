package com.tradingjournal.backend.dto.trade;

import com.tradingjournal.backend.enums.TradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeResponseDTO {
    private Long id;
    private Long accountId;
    private String instrument;
    private TradeType type;
    private int quantity;
    private BigDecimal entryPrice;
    private BigDecimal exitPrice;
    private LocalDateTime entryDateTime;
    private LocalDateTime exitDateTime;
    private BigDecimal stopLoss;
    private BigDecimal targetPrice;
    private BigDecimal profitLoss;
    private String notes;
}
