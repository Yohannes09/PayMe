package com.payme.authentication.diffMicroServ.dto;

import com.payme.authentication.diffMicroServ.constants.AccountType;
import com.payme.authentication.diffMicroServ.constants.Currency;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AccountDto{
            private UUID accountId;
            private UUID userId;
            private BigDecimal balance;
            private AccountType accountType;
            private Currency currency;
            private boolean isActive;
}
