package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.dto.TransferResponseDtoOLD;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RequestMapping("api/tenmo/transfer")
@RestController
public class TransferController {
    //private final TransferService transferService;
    private final AccountService accountService;
    private final UserService userService;

    public TransferController(TransferService transferService, AccountService accountService, UserService userService) {
        //this.transferService = transferService;
        this.accountService = accountService;
        this.userService = userService;
    }

    public TransferController() {
        //this.transferService = new RestTransferService();
        this.accountService = new RestAccountService();
        this.userService = new RestUserService();
    }


    @PostMapping("/{transferTypeId}")
    public ResponseEntity<TransferResponseDto> processTransfer(@Valid @RequestBody TransferDto transferDto,
                                                                  @PathVariable("transferTypeId") int transferTypeId) {
        Optional<Transfer> transfer = transferService.processTransfer(
                transferTypeId,
                transferDto.getSenderAccountId(),
                transferDto.getRecipientAccountId(),
                transferDto.getAmount()
        );

        return transfer.map(t -> {
            TransferResponseDto dto = new TransferResponseDto(
                    t.getTransferId(),
                    t.getSenderAccountId(),
                    t.getRecipientAccountId(),
                    t.getTransferStatusId(),
                    t.getTypeId(),
                    t.getAmount());
            return ResponseEntity.ok(dto);
            }
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{transferId}")
    public ResponseEntity<TransferResponseDtoOLD> getTransferById(@PathVariable("transferId") int transferId) {
        Optional<Transfer> transfer = transferService.getTransferById(transferId);

        return transfer.map(t -> {
            TransferResponseDtoOLD transferDtos = new TransferResponseDtoOLD(
                    t.getTransferId(),
                    t.getSenderAccountId(),
                    t.getRecipientAccountId(),
                    t.getTransferStatusId(),
                    t.getTypeId(),
                    t.getAmount());
            return ResponseEntity.ok(transferDtos);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*  Returns an account's transactions. */
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<TransferResponseDto>> accountTransferHistory(@PathVariable("accountId") int accountId) {
        List<TransferResponseDto> transfers = transferService.getAccountHistory(accountId);

        return transfers.isEmpty() ?
                ResponseEntity.badRequest().build(): ResponseEntity.ok(transfers);
    }

    @GetMapping("/transfer-status/{accountId}/{transferStatusId}")
    public ResponseEntity<List<TransferResponseDto>> getAccountTransferStatus(@PathVariable("accountId") Long accountId,
                                                                              @PathVariable("transferStatusId") int transferStatusId){
        List<TransferResponseDto> transfers = transferService.accountTransferStatus(transferStatusId, accountId);
        return transfers.isEmpty() ? ResponseEntity.badRequest().build() : ResponseEntity.ok(transfers);
    }

    @PostMapping("/pending/{transferId}/{transferStatusId}")
    public ResponseEntity<Optional<Transfer>> updatePendingTransfer(@PathVariable("transferId") int transferId,
                                                                    @PathVariable("transferStatusId") int newTransferStatusId){
        Optional<Transfer> transfer = transferService.updatePendingTransfer(transferId, newTransferStatusId);
        return transfer.isPresent() ? ResponseEntity.ok(transfer) : ResponseEntity.badRequest().build();
    }

}