package com.tenmo.app.mapper;

import com.tenmo.app.entity.Transfer;
import com.tenmo.app.dto.transfer.TransferRequestDto;

import java.math.BigDecimal;

public class TransferMapper {

    public static Transfer mapRequestToTransfer(TransferRequestDto requestDto){

        return Transfer.builder()
                .amount(requestDto.amount().multiply(BigDecimal.valueOf(requestDto.accountToIds().size())))
                .accountFrom(requestDto.accountFromId())
                .accountTo(requestDto.accountToIds())
                .transferMessage(requestDto.transferMessage().orElse(""))
                .currency(requestDto.currency())
                .build();
    }

}
