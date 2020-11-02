package com.github.bysrkh.mitraisatmsimulation.service;

import com.github.bysrkh.mitraisatmsimulation.component.InputHelper;
import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;
import com.github.bysrkh.mitraisatmsimulation.repository.AccountRepository;
import com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class TransactionHistoryService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InputHelper inputHelper;

    public int printTransactionHistory(Result<Account> result) {
        Account account = accountRepository.getAccount(result.getResult());

        inputHelper.prompt(String.format(
                "Transaction History\n%s\n%s\nPress enter to continue ",
                "Account Number | Current Balance | Credit | Debit",
                account.getBalanceHistories()
                        .stream()
                        .limit(10)
                        .map(balanceHistory ->
                                String.format(
                                        "%s | %s | %s | %s\n", balanceHistory.getAccountNumber(),
                                        balanceHistory.getCurrentBalance(),
                                        balanceHistory.getCreditedBalance(),
                                        balanceHistory.getDebitedBalance())).collect(Collectors.joining(""))));

        return NavigationConstant.TO_TRANSACTION.getValue();

    }
}
