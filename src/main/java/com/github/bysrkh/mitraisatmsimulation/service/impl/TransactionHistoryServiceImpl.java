package com.github.bysrkh.mitraisatmsimulation.service.impl;

import com.github.bysrkh.mitraisatmsimulation.component.InputHelper;
import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;
import com.github.bysrkh.mitraisatmsimulation.repository.AccountRepository;
import com.github.bysrkh.mitraisatmsimulation.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InputHelper inputHelper;

    @Override
    public void printTransactionHistory(Result<Account> result) {
        Account account = accountRepository.getAccount(result.getResult());

        inputHelper.prompt(String.format(
                "Transaction History\n%s\n%s\nPress enter to continue ",
                "Account Number | Current Balance | Credit | Debit",
                account.getBalanceHistories()
                        .stream()
                        .map(balanceHistory ->
                                String.format(
                                        "%s | %s | %s | %s\n", balanceHistory.getAccountNumber(),
                                        balanceHistory.getCurrentBalance(),
                                        balanceHistory.getCreditedBalance(),
                                        balanceHistory.getDebitedBalance())).collect(Collectors.joining(""))));

    }
}
