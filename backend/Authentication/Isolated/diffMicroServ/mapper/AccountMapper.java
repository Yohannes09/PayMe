package com.payme.authentication.diffMicroServ.mapper;

import com.payme.authentication.diffMicroServ.dto.AccountDto;
import com.payme.authentication.diffMicroServ.entity.Account;

public class AccountMapper {

    public static AccountDto mapAccountToDto(Account account){
        return null;
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
