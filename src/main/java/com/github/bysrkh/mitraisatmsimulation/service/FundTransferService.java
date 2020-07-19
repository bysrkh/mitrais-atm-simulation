package com.github.bysrkh.mitraisatmsimulation.service;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;

public interface FundTransferService {

    void inputTransferedAccount(Result<Account> account);

    void isValidData(Result<Account> result);

    void transferAmount(Result<Account> result);

    void confirmTransaction(Result<Account> result);
}
