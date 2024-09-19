package com.tenmo.controller;

import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.dto.transfer.TransferRequestDto;
import com.tenmo.entity.Transfer;
import com.tenmo.services.main.AccountService;
import com.tenmo.services.main.TransferService;
import com.tenmo.services.main.UserService;
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
    public ResponseEntity<Transfer> processTransfer(@RequestBody @Valid TransferRequestDto requestDto,
                                                    @PathVariable("transferTypeId") Integer transferTypeId) {
        return ResponseEntity.ok(
                transferService.processTransferRequest(requestDto));
    }


    public ResponseEntity<Transfer> findTransferById(@PathVariable("transferId") Long transferId) {

        return ResponseEntity.ok(transferService.findTransferById());
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
        return null;
    }

    @PostMapping("/pending/{transferId}/{transferStatusId}")
    public ResponseEntity<Optional<Transfer>> updatePendingTransfer(@PathVariable("transferId") Long transferId,
                                                                    @PathVariable("transferStatusId") Integer newTransferStatusId){

        return null;
    }


}