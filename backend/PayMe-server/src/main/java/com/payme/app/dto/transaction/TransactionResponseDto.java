package com.payme.app.dto.transaction;

import com.payme.app.util.Currency;
import com.payme.app.util.TransactionStatus;
import com.payme.app.util.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *  Client response containing more information regarding a transfer. */
public record TransactionResponseDto(
        UUID transferId,
        String accountFromUsername,
        String accountToUsername,
        BigDecimal amount,
        String transferMessage,
        Currency currency,
        LocalDateTime createdAt,
        TransactionType type,
        TransactionStatus status
){

}
