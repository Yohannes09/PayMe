package com.techelevator.tests.service;

import com.tenmo.app.repository.AccountRepository;
import com.tenmo.app.repository.TransferRepository;
import com.tenmo.app.services.main.TransferService;
import com.tenmo.app.services.validation.ValidatorService;
import org.mockito.Mock;

public class TransferServiceTests {
    private TransferService transferService;
    private ValidatorService validatorService;

    @Mock private TransferRepository transferRepository;
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
