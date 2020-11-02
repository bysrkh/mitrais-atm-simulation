package com.github.bysrkh.mitraisatmsimulation.service;

import com.github.bysrkh.mitraisatmsimulation.component.OutputHelper;
import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.domain.BalanceHistory;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;
import com.github.bysrkh.mitraisatmsimulation.repository.AccountRepository;
import com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_OTHER_WITHDRAWAL;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_WITHDRAWAL;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.ValidConstant.INVALID;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.ValidConstant.VALID;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.WithdrawConstant.FIFTY_DOLLAR_OPTION;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.WithdrawConstant.FIFTY_DOLLAR_VALUE;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.WithdrawConstant.ONE_HUNDRED_DOLLAR_OPTION;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.WithdrawConstant.ONE_HUNDRED_DOLLAR_VALUE;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.WithdrawConstant.TEN_DOLLAR_OPTION;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.WithdrawConstant.TEN_DOLLAR_VALUE;

@Service
public class WithdrawalService {
    private static final int X = 1;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OutputHelper outputHelper;

    public Result<Account> withdrawFixedBalance(Result<Account> operatedAccountResult) {
        int deductedBalance = getFixedDeductedBalance(operatedAccountResult);

        Account existingAccount = accountRepository.getAccount(operatedAccountResult.getResult());
        Result<Account> checkedAccountResult = validateWithdrawFixedBalance(existingAccount, deductedBalance);
        if (INVALID == checkedAccountResult.getValid())
            return checkedAccountResult;

        Account updatedAccount = operateDeductionFromAccount(existingAccount, deductedBalance);
        updatedAccount = accountRepository.updateAccount(updatedAccount);

        return new Result(updatedAccount, "", NavigationConstant.TO_WITHDRAWAL_SUMMARY, VALID);
    }

    public Result<Account> withdrawOtherBalance(Result<Account> operatedAccountResult) {
        int deductedBalance = operatedAccountResult.getAdditionalNavigation();

        Account existingAccount = accountRepository.getAccount(operatedAccountResult.getResult());
        Result chckAccountResult = validateWithdrawOtherBalance(existingAccount, deductedBalance);
        if (INVALID == chckAccountResult.getValid()) {
            outputHelper.print(chckAccountResult.getMessage());
            return chckAccountResult;
        }

        Account updatedAccount = operateDeductionFromAccount(existingAccount, deductedBalance);

        return new Result(updatedAccount,  "", NavigationConstant.TO_WITHDRAWAL_SUMMARY, VALID);
    }

    private int getFixedDeductedBalance(Result<Account> operatedAccountResult) {
        int deductedBalance = 0;

        switch (operatedAccountResult.getAdditionalNavigation()) {
            case TEN_DOLLAR_OPTION:
                deductedBalance = TEN_DOLLAR_VALUE;
                break;
            case FIFTY_DOLLAR_OPTION:
                deductedBalance = FIFTY_DOLLAR_VALUE;
                break;
            case ONE_HUNDRED_DOLLAR_OPTION:
                deductedBalance = ONE_HUNDRED_DOLLAR_VALUE;
                break;
            default:
                break;
        }
        return deductedBalance;
    }

    private Result<Account> validateWithdrawOtherBalance(Account existingAccount, int deductedBalance) {
        if (deductedBalance > 1000)
            return new Result(existingAccount,  "Maximum amount to withdraw is $1000", TO_OTHER_WITHDRAWAL, INVALID);
        else if (deductedBalance % 10 != 0)
            return new Result(existingAccount,  "Invalid ammount", TO_OTHER_WITHDRAWAL, INVALID);
        else if (existingAccount.getBalance() < deductedBalance)
            return new Result(existingAccount,  "Insufficient balance $" + deductedBalance, NavigationConstant.TO_WITHDRAWAL, INVALID);
        else
            return new Result(existingAccount,  "Unrecognized Error" + deductedBalance, TO_OTHER_WITHDRAWAL, VALID);
    }

    private Result<Account> validateWithdrawFixedBalance(Account existingAccount, int fixedDeductedBalance) {
        Result<Account> result = new Result<>(existingAccount,  "", TO_WITHDRAWAL, VALID);
        if (existingAccount.getBalance() < fixedDeductedBalance) {
            result.setMessage("Insufficient balance");
            result.setValid(INVALID);
        }

        outputHelper.print(result.getMessage());

        return result;
    }

    private Account operateDeductionFromAccount(Account existingAccount, int deductedBalance) {
        int remainBalance = existingAccount.getBalance() - deductedBalance;

        existingAccount.setBalance(remainBalance);
        existingAccount.getBalanceHistories().add(new BalanceHistory(existingAccount.getAccountNumber(), existingAccount.getBalance(), 0,  deductedBalance));

        return existingAccount;
    }
}
