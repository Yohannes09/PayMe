package com.tenmo.mapper;

import com.tenmo.dto.transfer.TransferDto;
import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.entity.Transfer;

import java.util.Optional;

public class TransferMapper {

    public static Transfer mapRequestToTransfer(TransferRequestDto requestDto){

        return Transfer.builder()
                .amount(requestDto.amount())
                .accountFrom(requestDto.accountFromId())
                .accountTo(requestDto.accountToId())
                .transferMessage(requestDto.transferMessage().orElse(""))
                .currency(requestDto.currency())
                .build();
    }


    public static TransferDto mapTransferToDto(Transfer transfer){
        return new TransferDto();
//                transfer.getTransferId(),
//                transfer.getAccountFrom(),
//                transfer.getAccountTo(),
//                transfer.getAmount(),
//                Optional.ofNullable(transfer.getTransferMessage()),
//                Optional.ofNullable(transfer.getCurrency()),
//                transfer.getCreatedAt()
//        );
    }

}
