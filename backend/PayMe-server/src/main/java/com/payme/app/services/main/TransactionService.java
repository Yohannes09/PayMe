package com.payme.app.services.main;

import com.payme.app.dto.transaction.TransactionRequestDto;
import com.payme.app.dto.transaction.TransactionResponseDto;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    List<TransactionResponseDto> findAllByTransferId(List<UUID> transferIds);

    TransactionResponseDto handleDirectTransfer(TransactionRequestDto request);

    TransactionResponseDto handleTransferRequest(TransactionRequestDto request);

    TransactionResponseDto handleApprovedRequest(UUID transferId);

    TransactionResponseDto handleRejectedRequest(UUID transferId);
}
