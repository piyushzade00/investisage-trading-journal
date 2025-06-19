package com.tradingjournal.backend.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tradingjournal.backend.dto.transaction.TransactionRequestDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {
    @NotBlank(message = "Account name is required")
    private String name;
    private String broker;

    @JsonProperty("isPrimary")
    private boolean isPrimary;

    private List<TransactionRequestDTO> transactions;
}
