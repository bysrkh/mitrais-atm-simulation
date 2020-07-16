package com.github.bysrkh.mitraisatmsimulation.service;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;

public interface AccountService {
    Result<Account> deductAccount(Account account);
    Result<Account> inputAccount();
    Result<Account> isExistingAccount(String accountNumber);

}
