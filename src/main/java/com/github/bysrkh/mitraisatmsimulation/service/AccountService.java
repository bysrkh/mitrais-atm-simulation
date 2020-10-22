package com.github.bysrkh.mitraisatmsimulation.service;

import com.github.bysrkh.mitraisatmsimulation.component.InputHelper;
import com.github.bysrkh.mitraisatmsimulation.component.OutputHelper;
import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;
import com.github.bysrkh.mitraisatmsimulation.repository.AccountRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_TRANSACTION;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_WELCOME_SCREEN;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.ValidConstant.INVALID;

@Service
public class AccountService {

    @Autowired
    private InputHelper inputHelper;

    @Autowired
    private OutputHelper outputHelper;

    @Autowired
    private AccountRepository accountRepository;

    public Result<Account> inputAccount(Result<Account> request) {
        Account requestedAccount = request.getResult();

        do {
            requestedAccount.setAccountNumber(inputHelper.prompt("Enter requestedAccount number"));
        } while (StringUtils.isBlank(request.getResult().getAccountNumber()));

        do {
            requestedAccount.setPin(inputHelper.prompt("Enter PIN"));
        } while (StringUtils.isBlank(request.getResult().getPin()));

        Result<Account> chkAccountResult = validateInputAccount(requestedAccount);
        if (INVALID == chkAccountResult.getValid()) {
            outputHelper.print(chkAccountResult.getMessage());
        }
        System.out.print("ululu");

        return chkAccountResult;
    }


    public Result<Account> validateInputAccount(Account requestedAccount) {
        Result<Account> result = new Result(requestedAccount, 0, "", TO_WELCOME_SCREEN.getValue(), INVALID);

        if (requestedAccount.getAccountNumber().length() != 6) {
            result.setMessage("Account Number should have 6 digits length");
            return result;
        }
        if (!requestedAccount.getAccountNumber().matches("[0-9]+")) {
            result.setMessage("Account Number should only contains numbers");
            return result;
        }
        Account existingAccount = accountRepository.getAccount(requestedAccount);
        if ((existingAccount == null && !requestedAccount.equals(existingAccount))) {
            result.setMessage("Invalid Account Number/PIN");
            return result;
        }

        return new Result(existingAccount, 0, "", TO_TRANSACTION.getValue(), INVALID);
    }
}
