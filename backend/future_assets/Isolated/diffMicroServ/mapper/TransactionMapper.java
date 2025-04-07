package com.payme.authentication.diffMicroServ.mapper;

import com.payme.authentication.diffMicroServ.dto.TransactionResponseDto;
import com.payme.authentication.diffMicroServ.entity.Transaction;
import com.payme.authentication.diffMicroServ.dto.TransactionRequestDto;

import java.math.BigDecimal;

public class TransactionMapper {

    public static Transaction mapRequestToTransaction(TransactionRequestDto requestDto){

        return Transaction.builder()
                .amount(requestDto.getAmount().multiply(BigDecimal.valueOf(requestDto.getAccountToIds().size())))
                .accountFrom(requestDto.getAccountFromId())
                .accountTo(requestDto.getAccountToIds())
                .transferMessage(requestDto.getTransferMessage().orElse(""))
                .currency(requestDto.getCurrency())
                .build();
    }

    public static TransactionResponseDto mapTransactionToResponse(Transaction transaction){
        return TransactionResponseDto.builder()
                .build();
    }

}
