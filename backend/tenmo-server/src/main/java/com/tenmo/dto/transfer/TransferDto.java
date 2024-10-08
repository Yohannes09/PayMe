package com.tenmo.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Common data structure for existing transfers.*/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {
    private @NotNull(message = "Transfer Id cannot be null. ") Long transferId;
    private @NotNull(message = "Sender ID cannot be null. ") Long accountFromId;
    private @NotNull(message = "Recipient ID cannot be null. ") Long accountToId;
    private @Min(value = 0, message = "Enter amount greater than zero.") BigDecimal amount;
    private Optional<String> transferMessage = Optional.empty();
    private Optional<String> currency = Optional.empty();
    private LocalDateTime createdAt;
}
