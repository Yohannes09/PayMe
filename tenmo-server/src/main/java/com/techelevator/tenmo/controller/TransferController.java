package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.dto.TransferHistoryDto;
import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("api/tenmo/transfer")
@RestController
public class TransferController {
    private static final Map<Integer, String> VALID_TYPE_IDS = getValidTransferTypes();
    private static final Map<Integer, String> VALID_STATUS_IDS = getValidTransferStatus();

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


    @PostMapping("/{transferTypeId}")
    public ResponseEntity<TransferResponseDto> processTransfer(@Valid @RequestBody TransferDto transferDto,
                                                               @PathVariable("transferTypeId") int transferTypeId) {
        if(!VALID_TYPE_IDS.containsKey(transferTypeId))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Transfer> newTransfer = transferService.processTransfer(
                transferTypeId,
                transferDto.getSenderAccountId(),
                transferDto.getRecipientAccountId(),
                transferDto.getAmount()
        );

        if(newTransfer.isPresent())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return newTransfer
                .map(transfer -> new ResponseEntity<>(
                        new TransferResponseDto(
                                newTransfer.get().getTransferId(),
                                transfer.getSenderAccountId(),
                                transfer.getRecipientAccountId(),
                                transfer.getTransferStatusId(),
                                transfer.getTypeId(),
                                transfer.getAmount()
                        ), HttpStatus.OK)
                )
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    /*  Returns an account's transactions. */
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<TransferHistoryDto>> accountTransferHistory(@PathVariable("accountId") int accountId) {
        List<TransferHistoryDto> transfers = transferService.getAccountHistory(accountId);

        if (!transfers.isEmpty()) {
            return new ResponseEntity<>(transfers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{transferId}")
    public ResponseEntity<TransferResponseDto> getTransferById(@PathVariable("transferId") int transferId) {
        Optional<Transfer> transfer = transferService.getTransferById(transferId);

        if (transfer.isPresent()) {

            TransferResponseDto transferDtos = new TransferResponseDto(
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

        return new ResponseEntity<>(HttpStatus.OK);

    }

    private static Map<Integer, String> getValidTransferTypes(){
        Map<Integer, String> validTransferIds = new HashMap<>();
        validTransferIds.put(1, null);
        validTransferIds.put(2, null);
        validTransferIds.put(3, null);
        return validTransferIds;
    }

    private static Map<Integer, String> getValidTransferStatus(){
        Map<Integer, String> validTransferStatusId = new HashMap<>();
        validTransferStatusId.put(1, null);
        validTransferStatusId.put(2, null);
        validTransferStatusId.put(3, null);
        return validTransferStatusId;
    }

}
