package com.github.bysrkh.mitraisatmsimulation.service.impl;

import com.github.bysrkh.mitraisatmsimulation.component.InputHelper;
import com.github.bysrkh.mitraisatmsimulation.component.OutputHelper;
import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.domain.BalanceHistory;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;
import com.github.bysrkh.mitraisatmsimulation.repository.AccountRepository;
import com.github.bysrkh.mitraisatmsimulation.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.bysrkh.mitraisatmsimulation.NavigationConstant.INVALID;
import static com.github.bysrkh.mitraisatmsimulation.NavigationConstant.VALID;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private InputHelper inputHelper;

    @Autowired
    private OutputHelper outputHelper;

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public void deductAccount(Result<Account> result) {
        Account requestedAccount = result.getResult();
        result.setChoose(INVALID);
        result.setMessage(String.format("Insufficient balance $%s", requestedAccount.getDeductedBalance()));

        Account existingAccount = accountRepository.getAccount(requestedAccount);
        int remainBalance = existingAccount.getBalance() - requestedAccount.getDeductedBalance();

        if (remainBalance < 0) {
            outputHelper.print(result.getMessage());
            return;
        }

        existingAccount.setBalance(remainBalance);
        existingAccount.setDeductedBalance(requestedAccount.getDeductedBalance());
        existingAccount.getBalanceHistories().add(new BalanceHistory(
                existingAccount.getAccountNumber(),
                existingAccount.getBalance(),
                0,
                existingAccount.getDeductedBalance()
        ));
        accountRepository.updateAccount(existingAccount);

        result.setChoose(VALID);
        result.setResult(existingAccount);
        result.setMessage("");
    }

    @Override
    public void inputAccount(Result<Account> result) {
        result.setChoose(INVALID);
        Account requestedAccount = result.getResult();

        do {
            requestedAccount.setAccountNumber(inputHelper.prompt("Enter requestedAccount number"));
        } while (StringUtils.isBlank(result.getResult().getAccountNumber()));

        do {
            requestedAccount.setPin(inputHelper.prompt("Enter PIN"));
        } while (StringUtils.isBlank(result.getResult().getPin()));

        if (requestedAccount.getAccountNumber().length() != 6) {
            result.setMessage("Account Number should have 6 digits length");

            return;
        }
        if (!requestedAccount.getAccountNumber().matches("[0-9]+")) {
            result.setMessage("Account Number should only contains numbers");

            return;
        }
        Account existingAccount = accountRepository.getAccount(requestedAccount);
        if ((existingAccount == null && !result.getResult().equals(existingAccount))) {
            result.setMessage("Invalid Account Number/PIN");

            return;
        }
        result.setResult(existingAccount);
        result.setChoose(VALID);
    }

    @Override
    public Result<Account> isExistingAccount(String accountNumber) {
        Account existingAccount = accountRepository.getAccount(new Account(accountNumber));
        Result<Account> result = null;
        if ((existingAccount == null && !result.getResult().equals(existingAccount))) {
            result.setMessage("Invalid Account Number/PIN");
            result.setChoose(INVALID);

        }
        return result;
    }
}
