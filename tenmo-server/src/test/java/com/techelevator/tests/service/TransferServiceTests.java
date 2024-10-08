package com.techelevator.tests.service;

import com.tenmo.repository.AccountRepository;
import com.tenmo.repository.TransferRepository;
import com.tenmo.services.main.TransferService;
import com.tenmo.services.validation.ValidatorService;
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
