package com.github.bysrkh.mitraisatmsimulation.service;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;

public interface AccountService {
    void deductAccount(Result<Account> result);

    void inputAccount(Result<Account> result);

    Result<Account> isExistingAccount(String accountNumber);

}
