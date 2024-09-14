package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.dto.TransferRequestDto;
import com.techelevator.tenmo.entity.Transfer;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@RequestMapping("api/tenmo/transfer")
@RestController
public class TransferController {
    private final TransferService transferService;
    private final AccountService accountService;
    private final UserService userService;

    public TransferController(TransferService transferService, AccountService accountService, UserService userService) {
        this.transferService = transferService;
        this.accountService = accountService;
        this.userService = userService;
    }


    @PostMapping("/{transferTypeId}")
    public ResponseEntity<TransferResponseDto> processTransfer(@RequestBody @Valid TransferRequestDto requestDto,
                                                               @PathVariable("transferTypeId") Integer transferTypeId) {
        Optional<Transfer> newTransfer = transferService.processTransfer(
                transferTypeId,
                requestDto.getAccountFromId(),
                requestDto.getAccountToId(),
                requestDto.getAmount(),
                requestDto.getTransferMessage().get()
        );

        return newTransfer
                .map(transfer -> transferService.getDetailedTransfer(transfer.getTransferId()).orElseThrow(() -> new DaoException("")))
                .map(responseDto -> ResponseEntity.ok(responseDto))
                .orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }


    public ResponseEntity<TransferResponseDto> getTransferById(@PathVariable("transferId") Long transferId) {

        return transferService.getDetailedTransfer(transferId)
                .map(dto -> ResponseEntity.ok(dto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /*  Returns an account's transactions. */
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<TransferResponseDto>> accountTransferHistory(@PathVariable("accountId") Long accountId) {
        List<TransferResponseDto> transfers = transferService.getAccountHistory(accountId);

        return transfers.isEmpty() ?
                ResponseEntity.badRequest().build(): ResponseEntity.ok(transfers);
    }

    @GetMapping("/transfer-status/{accountId}/{transferStatusId}")
    public ResponseEntity<List<TransferResponseDto>> getAccountTransferStatus(@PathVariable("accountId") Long accountId,
                                                                              @PathVariable("transferStatusId") Integer transferStatusId){
        List<TransferResponseDto> transfers = transferService.accountTransferStatus(transferStatusId, accountId);
        return transfers.isEmpty() ? ResponseEntity.badRequest().build() : ResponseEntity.ok(transfers);
    }

    @PostMapping("/pending/{transferId}/{transferStatusId}")
    public ResponseEntity<Optional<Transfer>> updatePendingTransfer(@PathVariable("transferId") Long transferId,
                                                                    @PathVariable("transferStatusId") Integer newTransferStatusId){
        Optional<Transfer> transfer = transferService.updatePendingTransfer(transferId, newTransferStatusId);
        return transfer.isPresent() ? ResponseEntity.ok(transfer) : ResponseEntity.badRequest().build();
    }


}