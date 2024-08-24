package com.techelevator.tenmo.service.useless;

import com.techelevator.tenmo.repository.*;
import org.springframework.stereotype.Service;

@Service
public class RestTenmoService implements TenmoService {
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public RestTenmoService(TransferRepository transferRepository,
                            AccountRepository accountRepository,
                            UserRepository userRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public RestTenmoService(){
        this.transferRepository = new JdbcTransferRepository();
        this.accountRepository = new JdbcAccountRepository();
        this.userRepository = new JdbcUserRepository();
    }



}
