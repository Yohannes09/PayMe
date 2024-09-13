package com.techelevator.tenmo.controller;

import org.springframework.web.bind.annotation.*;

@RequestMapping("api/tenmo/account")
@RestController
public class AccountController {
//    private final AccountService accountService;
//
//    public AccountController(AccountService accountService) {
//        this.accountService = accountService;
//    }
//
//    public AccountController() {
//        this.accountService = new RestAccountService();
//    }
//
//
//    @GetMapping("/{accountId}")
//    public ResponseEntity<AccountDto> getAccountByid(@PathVariable("accountId") int accountId) {
//        Optional <Account> account = accountService.getAccountById(accountId);
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

//    @PostMapping("")
//    public boolean create
}
