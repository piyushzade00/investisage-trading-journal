package com.tradingjournal.backend.dto.trade;

import com.tradingjournal.backend.enums.TradeType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TradeRequestDTO {
    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotBlank(message = "Instrument is required")
    private String instrument;

    @NotNull(message = "Trade type (BUY/SELL) is required")
    private TradeType type;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "Entry price is required")
    @DecimalMin(value = "0.01", message = "Entry price must be positive")
    private BigDecimal entryPrice;

    @DecimalMin(value = "0.00", message = "Exit price must be non-negative")
    private BigDecimal exitPrice;

    @NotNull(message = "Entry date and time is required")
    private LocalDateTime entryDateTime;

    private LocalDateTime exitDateTime;

    @DecimalMin(value = "0.00", message = "Stop loss must be non-negative")
    private BigDecimal stopLoss;

    @DecimalMin(value = "0.00", message = "Target price must be non-negative")
    private BigDecimal targetPrice;

    private String notes;
}
