package com.tradingjournal.backend.mapper;

import com.tradingjournal.backend.dto.transaction.TransactionRequestDTO;
import com.tradingjournal.backend.dto.transaction.TransactionResponseDTO;
import com.tradingjournal.backend.entity.AccountEntity;
import com.tradingjournal.backend.entity.TransactionEntity;
import org.springframework.stereotype.Component;

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

    public TransactionEntity toTransactionEntity(AccountEntity account, TransactionRequestDTO request){
        TransactionEntity transaction = TransactionEntity.builder()
                .account(account)
                .type(request.getType())
                .amount(request.getAmount())
                .transactionDateTime(request.getTransactionDateTime())
                .notes(request.getNotes())
                .build();

        return transaction;
    }
}
