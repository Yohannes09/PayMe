package com.payme.app.mapper;

import com.payme.app.dto.account.AccountDto;
import com.payme.app.entity.Account;

public class AccountMapper {

    public static AccountDto mapAccountToDto(Account account){
        return new AccountDto();
//                account.getAccountId(),
//                account.getUserId(),
//                account.getBalance(),
//                account.getAccountType(),
//                account.getCurrency(),
//                account.isActive(),
//                account.getCreatedAt()
//        );
    }

    public static Account mapDtoToAccount(AccountDto accountDto){
//        return new Account(
//                accountDto.getAccountId(),
//                accountDto.getUserId(),
//                accountDto.getBalance(),
//                accountDto.getAccountType(),
//                accountDto.getCurrency());
        return new Account();
    }
}