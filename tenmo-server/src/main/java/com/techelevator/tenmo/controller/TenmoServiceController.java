package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dto.TransferDto;
import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.service.TenmoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@RequestMapping("api/v1/tenmo")
@RestController
public class TenmoServiceController {
    private final TenmoService tenmoService;

    public TenmoServiceController(TenmoService tenmoService) {
        this.tenmoService = tenmoService;
    }


    @PostMapping("/transfer")
    public ResponseEntity<TransferResponseDto> processTransfer(@Valid @RequestBody TransferDto transferDto) {

        Optional<Transfer> transfer = tenmoService.processTransfer(
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


    @PermitAll
    @GetMapping("tenmo/transfer/{id}")
    public ResponseEntity<List<TransferDto>> accountTransferHistory(@PathVariable("id") int id) {
        List<Transfer> transfers = tenmoService.accountTransferHistory(id);

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

    @GetMapping("/print/{name}")
    public ResponseEntity<String> print(@PathVariable String name){
        return new ResponseEntity<>("hello " + name, HttpStatus.OK);
    }

}
