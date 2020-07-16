package com.github.bysrkh.mitraisatmsimulation.service.impl;

import com.github.bysrkh.mitraisatmsimulation.component.InputHelper;
import com.github.bysrkh.mitraisatmsimulation.component.OutputHelper;
import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.dto.AccountRepository;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;
import com.github.bysrkh.mitraisatmsimulation.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private InputHelper inputHelper;

    @Autowired
    private OutputHelper outputHelper;


    @Override
    public Result<Account> deductAccount(Account requestedAccount) {
        Result<Account> result = new Result<>(requestedAccount, 0, String.format("Insufficient balance $%s", requestedAccount.getDeductedBalance()));

        Account existingAccount = AccountRepository.getAccounts().get(requestedAccount.getAccountNumber());
        int remainBalance = existingAccount.getBalance() - requestedAccount.getDeductedBalance();

        if (remainBalance >= 0) {
            existingAccount.setBalance(remainBalance);
            existingAccount.setDeductedBalance(requestedAccount.getDeductedBalance());
            AccountRepository.getAccounts().put(existingAccount.getAccountNumber(), existingAccount);
            result = new Result<>(existingAccount, 1, "");
        } else {
            outputHelper.print(result.getMessage());
        }

        return result;
    }

    @Override
    public Result<Account> inputAccount() {
        Result<Account> result = new Result<>(new Account(), 1, "");
        do {
            result.getResult().setAccountNumber(inputHelper.prompt("Enter account number"));
        } while (StringUtils.isBlank(result.getResult().getAccountNumber()));

        do {
            result.getResult().setPin(inputHelper.prompt("Enter PIN"));
        } while (StringUtils.isBlank(result.getResult().getPin()));

        if (result.getResult().getAccountNumber().length() != 6) {
            result.setMessage("Account Number should have 6 digits length");

            return result;
        }
        if (!result.getResult().getAccountNumber().matches("[0-9]+")) {
            result.setMessage("Account Number should only contains numbers");

            return result;
        }
        Account existingAccount = AccountRepository.getAccounts().get(result.getResult().getAccountNumber());
        if ((existingAccount == null && !result.getResult().equals(existingAccount))) {
            result.setMessage("Invalid Account Number/PIN");

            return result;
        }
        result.setResult(existingAccount);
        result.setChoose(1);

        return result;
    }

    @Override
    public Result<Account> isExistingAccount(String accountNumber) {
        Account existingAccount = AccountRepository.getAccounts().get(accountNumber);
        Result<Account> result = new Result<>(existingAccount, 1, "");
        if ((existingAccount == null && !result.getResult().equals(existingAccount))) {
            result.setMessage("Invalid Account Number/PIN");
            result.setChoose(0);

            return result;
        }
        return result;
    }
}
