package com.tenmo.dto.transfer;

import com.tenmo.util.Currency;
import com.tenmo.util.TransferStatus;
import com.tenmo.util.TransferType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *  Client response containing more information regarding a transfer. */
public record TransferResponseDto(
        UUID transferId,
        String accountFromUsername,
        String accountToUsername,
        BigDecimal amount,
        String transferMessage,
        Currency currency,
        LocalDateTime createdAt,
        TransferType type,
        TransferStatus status){

}
