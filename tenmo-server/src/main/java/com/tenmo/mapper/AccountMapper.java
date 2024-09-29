package com.tenmo.mapper;

import com.tenmo.dto.account.AccountDto;
import com.tenmo.entity.Account;

import java.util.List;
import java.util.stream.Collectors;

public class AccountMapper {

    public static AccountDto mapAccountToDto(Account account){
        return new AccountDto(
                account.getAccountId(),
                account.getUserId(),
                account.getBalance(),
                account.getAccountType(),
                account.getCurrency(),
                account.isActive(),
                account.getCreatedAt()
        );
    }

    public static List<AccountDto> mapAccountToDto(List<Account> accounts){
        return accounts
                .stream()
                //.map(account -> mapDtoToAccount(account))
                .map(AccountMapper::mapAccountToDto)
                .collect(Collectors.toList());
    }
}
