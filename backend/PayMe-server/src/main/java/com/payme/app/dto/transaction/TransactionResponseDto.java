package com.payme.app.dto.transaction;

import com.payme.app.constants.Currency;
import com.payme.app.constants.TransactionStatus;
import com.payme.app.constants.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *  Client response containing more information regarding a transfer. */
@Builder
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
