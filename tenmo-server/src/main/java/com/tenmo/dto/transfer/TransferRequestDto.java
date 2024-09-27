package com.tenmo.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;

/**
 *  Client request when creating a new transfer.  */

@Getter
@AllArgsConstructor
public class TransferRequestDto {
//    private @NotNull Integer transferTypeId;
//    private @NotNull Integer transferStatusId;
    private @NotNull(message = "Sender ID cannot be null. ") Long accountFromId;
    private @NotNull(message = "Recipient ID cannot be null. ") Long accountToId;
    private @Min(value = 0, message = "Enter amount greater than zero.") BigDecimal amount;
    private Optional<String> transferMessage = Optional.empty();
    private Optional<String> currency = Optional.empty();
}
