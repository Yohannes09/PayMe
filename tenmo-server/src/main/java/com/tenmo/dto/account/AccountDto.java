package com.tenmo.dto.account;

import com.tenmo.util.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class AccountDto {

    private Long accountId;

    private Long userId;

    private BigDecimal balance;

    private AccountType accountType;

    private String currency;

    private boolean isActive;

    private LocalDateTime createdAt;

}
