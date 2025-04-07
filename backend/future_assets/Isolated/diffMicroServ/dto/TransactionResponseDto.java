package com.payme.authentication.diffMicroServ.dto;

import com.payme.authentication.diffMicroServ.constants.Currency;
import com.payme.authentication.diffMicroServ.constants.TransactionStatus;
import com.payme.authentication.diffMicroServ.constants.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *  Client response containing more information regarding a transfer. */
@Builder
@Getter
public class TransactionResponseDto{
    private UUID transferId;
    private String accountFromUsername;
    private String accountToUsername;
    private BigDecimal amount;
    private String transferMessage;
    private Currency currency;
    private LocalDateTime createdAt;
    private TransactionType type;
    private TransactionStatus status;
}
