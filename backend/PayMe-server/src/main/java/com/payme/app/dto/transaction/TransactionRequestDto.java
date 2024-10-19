package com.payme.app.dto.transaction;

import com.payme.app.constants.Currency;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *  Client request when creating a new transfer.  */

public record TransactionRequestDto(
        @NotNull(message = "Sender ID cannot be null. ") UUID accountFromId,
        @NotNull(message = "Recipient ID cannot be null. ") List<UUID> accountToIds,
        @Min(value = 0, message = "Enter amount greater than zero.") BigDecimal amount,
        @NotNull Currency currency,
        Optional<String> transferMessage){
}