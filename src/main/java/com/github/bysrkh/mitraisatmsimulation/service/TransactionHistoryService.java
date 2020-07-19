package com.github.bysrkh.mitraisatmsimulation.service;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;

public interface TransactionHistoryService {
    void printTransactionHistory(Result<Account> result);
}
