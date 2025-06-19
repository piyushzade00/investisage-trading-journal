package com.tradingjournal.backend.dto.account;

import com.tradingjournal.backend.dto.transaction.TransactionResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private Long id;
    private String name;
    private String broker;
    private Long userId;
    private boolean isPrimary;
    private List<TransactionResponseDTO> transactions;
    private BigDecimal accountBalance;
}
