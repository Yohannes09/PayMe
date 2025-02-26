package com.payme.app.mapper;

import com.payme.app.dto.transaction.TransactionResponseDto;
import com.payme.app.entity.Transaction;
import com.payme.app.dto.transaction.TransactionRequestDto;

import java.math.BigDecimal;

public class TransactionMapper {

    public static Transaction mapRequestToTransaction(TransactionRequestDto requestDto){

        return Transaction.builder()
                .amount(requestDto.amount().multiply(BigDecimal.valueOf(requestDto.accountToIds().size())))
                .accountFrom(requestDto.accountFromId())
                .accountTo(requestDto.accountToIds())
                .transferMessage(requestDto.transferMessage().orElse(""))
                .currency(requestDto.currency())
                .build();
    }

    public static TransactionResponseDto mapTransactionToResponse(Transaction transaction){
        return TransactionResponseDto.builder()
                .build();
    }

}
