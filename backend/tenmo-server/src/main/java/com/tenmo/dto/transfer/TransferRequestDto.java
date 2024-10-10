package com.tenmo.dto.transfer;

import com.tenmo.util.Currency;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 *  Client request when creating a new transfer.  */

public record TransferRequestDto(
        @NotNull(message = "Sender ID cannot be null. ") UUID accountFromId,
        @NotNull(message = "Recipient ID cannot be null. ") UUID accountToId,
        @Min(value = 0, message = "Enter amount greater than zero.") BigDecimal amount,
        @NotNull Currency currency,
        Optional<String> transferMessage){
}