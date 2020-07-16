package com.github.bysrkh.mitraisatmsimulation.service;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;

public interface FundTransferService {

    public Result<Account> inputTransferedAccount(Account account);

    Result<Account> isValidData(Account account);

    Result<Account> transferAmount(Account account);

    Result<Account> confirmTransaction(Account account);
}
