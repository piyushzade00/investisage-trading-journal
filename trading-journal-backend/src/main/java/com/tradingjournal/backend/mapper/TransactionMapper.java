package com.tradingjournal.backend.mapper;

import com.tradingjournal.backend.dto.transaction.TransactionRequestDTO;
import com.tradingjournal.backend.dto.transaction.TransactionResponseDTO;
import com.tradingjournal.backend.entity.AccountEntity;
import com.tradingjournal.backend.entity.TransactionEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {

    public TransactionResponseDTO mapToDto(TransactionEntity transaction) {
        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccount().getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .transactionDateTime(transaction.getTransactionDateTime())
                .notes(transaction.getNotes())
                .build();
    }

    public List<TransactionResponseDTO> mapToDto(List<TransactionEntity> transactions) {
        if (transactions == null) return new ArrayList<>();
        return transactions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TransactionEntity toTransactionEntity(AccountEntity account, TransactionRequestDTO request) {
        return TransactionEntity.builder()
                .id(request.getId())
                .account(account)
                .type(request.getType())
                .amount(request.getAmount())
                .transactionDateTime(request.getTransactionDateTime())
                .notes(request.getNotes())
                .build();
    }

    public List<TransactionEntity> toTransactionEntity(AccountEntity account, List<TransactionRequestDTO> requests) {
        if (requests == null) return new ArrayList<>();
        return requests.stream()
                .map(req -> toTransactionEntity(account, req))
                .collect(Collectors.toList());
    }
}
