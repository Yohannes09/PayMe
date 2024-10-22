package com.payme.app.dto.account;

import com.payme.app.constants.AccountType;
import com.payme.app.constants.Currency;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record AccountDto(
        UUID accountId,
        UUID userId,
        BigDecimal balance,
        AccountType accountType,
        Currency currency,
        boolean isActive) {
    //
}
