package com.tenmo.mapper;

import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.entity.Transfer;

import java.util.Optional;

public class TransferMapper {
    public static Transfer mapDtoToTransfer(TransferRequestDto requestDto){

        return new Transfer(
                requestDto.getTransferTypeId(),
                requestDto.getTransferStatusId(),
                requestDto.getAccountFromId(),
                requestDto.getAccountToId(),
                requestDto.getAmount(),
                requestDto.getTransferMessage().get(),
                requestDto.getCurrency().get()
        );
    }

    public static TransferRequestDto mapTransferToDto(Transfer request){
        return new TransferRequestDto(
                request.getTypeId(),
                request.getStatusId(),
                request.getAccountFrom(),
                request.getAccountTo(),
                request.getAmount(),
                Optional.ofNullable(request.getTransferMessage()),
                Optional.ofNullable(request.getCurrency())
        );
    }

}
