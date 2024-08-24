package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("api/tenmo/transfer")
@RestController
public class TransferServiceController {
    private final TransferService transferService;
    private final AccountService accountService;
    private final UserService userService;

    public TransferServiceController(TransferService transferService, AccountService accountService, UserService userService) {
        this.transferService = transferService;
        this.accountService = accountService;
        this.userService = userService;
    }

    public TransferServiceController() {
        this.transferService = new RestTransferService();
        this.accountService = new RestAccountService();
        this.userService = new RestUserService();
    }


    @PostMapping("")
    public ResponseEntity<TransferResponseDto> processTransfer(@Valid @RequestBody TransferDto transferDto) {

        Optional<Transfer> transfer = transferService.processTransfer(
                transferDto.getSenderAccountId(),
                transferDto.getRecipientAccountId(),
                transferDto.getAmount()
        );

        if (transfer.isPresent()) {
            Transfer clientResponse = transfer.get();

            TransferResponseDto transferResponseDto = new TransferResponseDto(
                    clientResponse.getSenderAccountId(),
                    clientResponse.getRecipientAccountId(),
                    clientResponse.getAmount()
            );
            return new ResponseEntity<>(transferResponseDto, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<TransferDto>> accountTransferHistory(@PathVariable("accountId") int accountId) {
        List<Transfer> transfers = transferService.accountTransferHistory(accountId);

        if (!transfers.isEmpty()) {
            List<TransferDto> transferDtos = transfers.
                    stream().
                    map(transfer -> new TransferDto(
                            transfer.getSenderAccountId(),
                            transfer.getRecipientAccountId(),
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
                    transfer.get().getSenderAccountId(),
                    transfer.get().getRecipientAccountId(),
                    transfer.get().getAmount());

            return new ResponseEntity<>(transferDtos, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/pending/{accountId}")
    public ResponseEntity<List<TransferDto>> getPendingTransfer(@PathVariable("accountId") int accountId){
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
