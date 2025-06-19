package com.tradingjournal.backend.mapper;

import com.tradingjournal.backend.dto.account.AccountRequestDTO;
import com.tradingjournal.backend.dto.account.AccountResponseDTO;
import com.tradingjournal.backend.entity.AccountEntity;
import com.tradingjournal.backend.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountEntity toAccountEntity(UserEntity user, AccountRequestDTO request){
        AccountEntity account = AccountEntity.builder()
                .name(request.getName())
                .broker(request.getBroker())
                .user(user)
                .isPrimary(request.isPrimary())
                .build();

        return account;
    }

    public AccountResponseDTO mapToDto(AccountEntity account) {
        return AccountResponseDTO.builder()
                .id(account.getId())
                .name(account.getName())
                .broker(account.getBroker())
                .userId(account.getUser().getId())
                .isPrimary(account.isPrimary())
                .accountBalance(account.getAccountBalance())
                .build();
    }
}
