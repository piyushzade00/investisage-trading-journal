package com.tradingjournal.backend.dto.transaction;

import com.tradingjournal.backend.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
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
public class TransactionRequestDTO {
    private Long id;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Transaction type (DEPOSIT/WITHDRAWAL) is required")
    private TransactionType type;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Transaction date and time is required")
    private LocalDateTime transactionDateTime;

    private String notes;
}
