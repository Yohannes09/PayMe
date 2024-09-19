package com.tenmo.dto;

import com.tenmo.util.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class AccountDto {

    private Integer accountId;

    private Integer userId;

    private BigDecimal balance;

    private AccountType accountType;

    private String currency;

    private boolean isActive;

    private LocalDateTime createdAt;

}
