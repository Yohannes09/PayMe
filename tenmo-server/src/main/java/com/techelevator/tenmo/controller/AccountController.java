package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dto.AccountDto;
import com.techelevator.tenmo.dto.TransferResponseDto;
import com.techelevator.tenmo.services.main.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/tenmo/account")
@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccountByid(@PathVariable("accountId") int accountId) {
        return accountService.findAccountById();
    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<AccountDto> getAccountByUserId(@PathVariable("userId") int userId) {
//        Optional <Account> account = accountService.getAccountByUserId(userId);
//
//        if (account.isPresent()) {
//
//            return new ResponseEntity<>(new AccountDto(
//                    account.get().getAccountId(),
//                    account.get().getUserId(),
//                    account.get().getBalance()
//            ), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<AccountDto>> getAccounts() {
//        List<Account> accounts = accountService.getAccounts();
//
//        if(accounts.isEmpty())
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//        return new ResponseEntity<>(
//                accounts
//                        .stream()
//                        .map(account -> new AccountDto(
//                                        account.getAccountId(),
//                                        account.getUserId(),
//                                        account.getBalance())
//                        ).collect(Collectors.toList()), HttpStatus.OK);
//    }

    /*  Returns an account's transactions. */
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<TransferResponseDto>> accountTransferHistory(@PathVariable("accountId") Long accountId) {
        List<TransferResponseDto> transfers = List.of();// = accountService.getAccountHistory(accountId);

        return transfers.isEmpty() ?
                ResponseEntity.badRequest().build(): ResponseEntity.ok(transfers);
    }
}
