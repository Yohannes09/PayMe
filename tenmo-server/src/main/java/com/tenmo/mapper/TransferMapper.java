package com.tenmo.mapper;

import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.entity.Transfer;

import java.util.Optional;

public class TransferMapper {

    public static Transfer mapRequestToTransfer(TransferRequestDto requestDto){

        return new Transfer(
                requestDto.getAccountFromId(),
                requestDto.getAccountToId(),
                requestDto.getAmount(),
                requestDto.getTransferMessage().get(),
                requestDto.getCurrency().get()
        );
    }

    public static TransferRequestDto mapTransferToDto(Transfer request){
        return new TransferRequestDto(
                request.getAccountFrom(),
                request.getAccountTo(),
                request.getAmount(),
                Optional.ofNullable(request.getTransferMessage()),
                Optional.ofNullable(request.getCurrency())
        );
    }

}
