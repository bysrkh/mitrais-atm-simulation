package com.github.bysrkh.mitraisatmsimulation.service;

import com.github.bysrkh.mitraisatmsimulation.component.OutputHelper;
import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.domain.BalanceHistory;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;
import com.github.bysrkh.mitraisatmsimulation.repository.AccountRepository;
import com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant;
import com.github.bysrkh.mitraisatmsimulation.util.constant.ValidConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
import static java.util.Arrays.asList;

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
        if (INVALID.getValue() == checkedAccountResult.getValid())
            return checkedAccountResult;

        Account updatedAccount = operateDeductionFromAccount(existingAccount, deductedBalance);
        updatedAccount = accountRepository.updateAccount(updatedAccount);

        return new Result(updatedAccount, 0, "", NavigationConstant.TO_WITHDRAWAL_SUMMARY.getValue(), ValidConstant.VALID.getValue());
    }

    public Result<Account> withdrawOtherBalance(Result<Account> operatedAccountResult) {
        int deductedBalance = operatedAccountResult.getOperation();

        Account existingAccount = accountRepository.getAccount(operatedAccountResult.getResult());
        Result chckAccountResult = validateWithdrawOtherBalance(existingAccount, deductedBalance);
        if (INVALID.getValue() == chckAccountResult.getValid()) {
            outputHelper.print(chckAccountResult.getMessage());
            return chckAccountResult;
        }

        Account updatedAccount = operateDeductionFromAccount(existingAccount, deductedBalance);

        return new Result(updatedAccount, 0, "", NavigationConstant.TO_WITHDRAWAL_SUMMARY.getValue(), ValidConstant.VALID.getValue());
    }

    private int getFixedDeductedBalance(Result<Account> operatedAccountResult) {
        int deductedBalance = 0;

        switch (operatedAccountResult.getOperation()) {
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
            return new Result(existingAccount, 0, "Maximum amount to withdraw is $1000", TO_OTHER_WITHDRAWAL.getValue(), INVALID.getValue());
        else if (deductedBalance % 10 != 0)
            return new Result(existingAccount, 0, "Invalid ammount", TO_OTHER_WITHDRAWAL.getValue(), INVALID.getValue());
        else if (existingAccount.getBalance() < deductedBalance)
            return new Result(existingAccount, 0, "Insufficient balance $" + deductedBalance, NavigationConstant.TO_WITHDRAWAL.getValue(), INVALID.getValue());
        else
            return new Result(existingAccount, 0, "Unrecognized Error" + deductedBalance, TO_OTHER_WITHDRAWAL.getValue(), VALID.getValue());
    }

    private Result<Account> validateWithdrawFixedBalance(Account existingAccount, int fixedDeductedBalance) {
        Result<Account> result = new Result<>(existingAccount, 0, "", TO_WITHDRAWAL.getValue(), VALID.getValue());
        if (existingAccount.getBalance() < fixedDeductedBalance) {
            result.setMessage("Insufficient balance");
            result.setValid(INVALID.getValue());
        }

        outputHelper.print(result.getMessage());

        return result;
    }

    private Account operateDeductionFromAccount(Account existingAccount, int deductedBalance) {
        int remainBalance = existingAccount.getBalance() - deductedBalance;

        existingAccount.setBalance(remainBalance);
        existingAccount.getBalanceHistories().add(new BalanceHistory(existingAccount.getAccountNumber(), existingAccount.getBalance(), 0, deductedBalance));

        return existingAccount;
    }
}
