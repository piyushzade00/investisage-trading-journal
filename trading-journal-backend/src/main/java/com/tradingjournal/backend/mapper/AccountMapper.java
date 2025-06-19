package com.tradingjournal.backend.mapper;

import com.tradingjournal.backend.dto.account.AccountRequestDTO;
import com.tradingjournal.backend.dto.account.AccountResponseDTO;
import com.tradingjournal.backend.entity.AccountEntity;
import com.tradingjournal.backend.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    private final TransactionMapper transactionMapper;

    @Autowired
    public AccountMapper(TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
    }

    public AccountEntity toAccountEntity(UserEntity user, AccountRequestDTO request){
        AccountEntity account = AccountEntity.builder()
                .name(request.getName())
                .broker(request.getBroker())
                .user(user)
                .isPrimary(request.isPrimary())
                .build();

        // Map DTOs to entities and set the account reference
        if (request.getTransactions() != null && !request.getTransactions().isEmpty()) {
            account.setTransactions(
                    transactionMapper.toTransactionEntity(account, request.getTransactions())
            );
        }

        return account;
    }

    public AccountResponseDTO mapToDto(AccountEntity account) {
        return AccountResponseDTO.builder()
                .id(account.getId())
                .name(account.getName())
                .broker(account.getBroker())
                .userId(account.getUser().getId())
                .isPrimary(account.isPrimary())
                .transactions(transactionMapper.mapToDto(account.getTransactions()))
                .accountBalance(account.getAccountBalance())
                .build();
    }
}
