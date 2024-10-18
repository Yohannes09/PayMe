package com.techelevator.tests.service;

import com.payme.app.repository.AccountRepository;
import com.payme.app.repository.TransactionRepository;
import com.payme.app.services.main.TransactionService;
import com.payme.app.services.validation.ValidatorService;
import org.mockito.Mock;

public class TransferServiceTests {
    private TransactionService transactionService;
    private ValidatorService validatorService;

    @Mock private TransactionRepository transactionRepository;
    @Mock private AccountRepository accountRepository;

//    @BeforeEach
//    void setUp(){
//        MockitoAnnotations.openMocks(this);
//        transferService = new TransferServiceImpl(
//                accountRepository,
//                transferRepository,
//                validatorService);
//    }
}
