package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.exception.AccountException;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.mapper.TransferMapper;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public TransferController() {
        this.transferService = new RestTransferService();
        this.accountService = new RestAccountService();
        this.userService = new RestUserService();
    }


    @PostMapping("")
    public ResponseEntity<TransferDto> processTransfer(@Valid @RequestBody TransferDto transferDto) {

        Optional<Transfer> newTransfer = transferService.processTransfer(
                transferDto.getSenderAccountId(),
                transferDto.getRecipientAccountId(),
                transferDto.getAmount()
        );

        return newTransfer
                .map(transfer -> new ResponseEntity<>(
                        new TransferDto(
                                transfer.getTransferId(),
                                transfer.getSenderAccountId(),
                                transfer.getRecipientAccountId(),
                                transfer.getTransferStatusId(),
                                transfer.getTypeId(),
                                transfer.getAmount()
                        ), HttpStatus.OK)
                )
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    /*  Returns an account's transactions. */
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<TransferDto>> accountTransferHistory(@PathVariable("accountId") int accountId) {
        List<Transfer> transfers = transferService.accountTransferHistory(accountId);

        if (!transfers.isEmpty()) {
            List<TransferDto> transferDtos = transfers.
                    stream().
                    map(transfer -> new TransferDto(
                            transfer.getTransferId(),
                            transfer.getSenderAccountId(),
                            transfer.getRecipientAccountId(),
                            transfer.getTransferStatusId(),
                            transfer.getTypeId(),
                            transfer.getAmount()
                    )).collect(Collectors.toList());
            return new ResponseEntity<>(transferDtos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{transferId}")
    public ResponseEntity<TransferDto> getTransferById(@PathVariable("transferId") int transferId) {
        Optional<Transfer> transfer = transferService.getTransferById(transferId);

        if (transfer.isPresent()) {

            TransferDto transferDtos = new TransferDto(
                    transfer.get().getTransferId(),
                    transfer.get().getSenderAccountId(),
                    transfer.get().getRecipientAccountId(),
                    transfer.get().getTransferStatusId(),
                    transfer.get().getTypeId(),
                    transfer.get().getAmount());

            return new ResponseEntity<>(transferDtos, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/transfer-status/{accountId}/{transferStatusId}")
    public ResponseEntity<List<TransferDto>> getAccountTransferStatus(@PathVariable("accountId") int accountId,
                                                                @PathVariable("transferStatusId") int transferStatusId){
        if(transferStatusId < 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<TransferDto> pendingTransfers = transferService.getAccountTransferStatus(accountId, transferStatusId)
                .stream()
                .map(transfer ->
                        new TransferDto(
                                transfer.getTransferId(),
                                transfer.getSenderAccountId(),
                                transfer.getRecipientAccountId(),
                                transfer.getTransferStatusId(),
                                transfer.getTypeId(),
                                transfer.getAmount()
                        )
                ).collect(Collectors.toList());

        return new ResponseEntity<>(pendingTransfers, HttpStatus.OK);

    }

}
