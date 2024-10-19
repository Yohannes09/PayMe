package com.payme.app.dto.account;

import com.payme.app.constants.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class AccountDto {

    private Long accountId;

    private Long userId;

    private BigDecimal balance;

    private AccountType accountType;

    private String currency;

    private boolean isActive;

    private LocalDateTime createdAt;

}
