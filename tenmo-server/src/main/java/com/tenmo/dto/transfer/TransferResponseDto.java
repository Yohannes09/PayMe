package com.tenmo.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *  Client response containing more information regarding a transfer. */
@AllArgsConstructor
@Getter
public class TransferResponseDto{
    private String accountFromUsername;
    private String accountToUsername;
    private BigDecimal amount;
    private String transferMessage;
    private String currency;
    private LocalDateTime createdAt;
    private String typeDescription;
    private String statusDescription;
}
