package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransactionDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.stereotype.Service;

@Service
public class RestTransactionService implements TransactionService{
    private final AccountDao accountDao;
    private final TransactionDao transactionDao;

    public RestTransactionService(AccountDao accountDao, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    @Override
    public boolean transferFunds(int senderId, int recipientId, double amountTransfered) {
        accountDao.withdraw(senderId, amountTransfered);
        accountDao.deposit(recipientId, amountTransfered);
        return false;
    }
}
