package com.github.bysrkh.mitraisatmsimulation.service;

import com.github.bysrkh.mitraisatmsimulation.component.InputHelper;
import com.github.bysrkh.mitraisatmsimulation.component.OutputHelper;
import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.domain.BalanceHistory;
import com.github.bysrkh.mitraisatmsimulation.domain.Menu;
import com.github.bysrkh.mitraisatmsimulation.domain.TransferredAccount;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;
import com.github.bysrkh.mitraisatmsimulation.repository.AccountRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_FUND_TRANSFER;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_FUND_TRANSFER_CONFIRMATION;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_FUND_TRANSFER_SUMMARY;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_TRANSACTION;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_WITHDRAWAL_SUMMARY;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.ValidConstant.INVALID;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.ValidConstant.VALID;
import static java.util.stream.Collectors.joining;

@Service
public class FundTransferService {

    @Autowired
    private InputHelper inputHelper;

    @Autowired
    private OutputHelper outputHelper;

    @Autowired
    private AccountRepository accountRepository;
    private Function<Map.Entry, String> extractValue = values -> String.format("%s. %s\n", values.getKey(), values.getValue());

    private Menu menu = new Menu();

    public Result<TransferredAccount> inputTransferedAccount() {
        Result<TransferredAccount> result = new Result<>(new TransferredAccount(), 0, "", TO_TRANSACTION.getValue(), INVALID);

        String accNo = inputHelper.prompt("Please enter destination account and press enter to continue or\npress enter to go back to Transaction ");
        if (StringUtils.isBlank(accNo)) {
            return result;
        }
        String trfAmount = inputHelper.prompt("Please enter transfer amount and\npress enter to continue or\npress enter to go back to Transaction ");
        if (StringUtils.isBlank(trfAmount)) {
            return result;
        }
        inputHelper.prompt("Reference Number: (This is an autogenerated random 6 digits number)\npress enter to continue");

        result.getResult().setAccountNumber(accNo);
        result.getResult().setTransferredAmount(trfAmount);
        result.getResult().setReferenceNumber("123456");
        result.setNavigation(TO_FUND_TRANSFER_CONFIRMATION.getValue());

        return result;
    }

    public Result<Account> transferAmount(TransferredAccount reqTrfAccount, Account reqAccount) {
        System.out.println(reqAccount.getBalance());
        System.out.println(reqTrfAccount.getTransferredAmount());
        Result<Account> chkAccResult = validateInputBeforeTransfer(reqTrfAccount, reqAccount);

        if (INVALID == chkAccResult.getValid()) return chkAccResult;

        Account transactionData = toAccount(reqTrfAccount);
        Account trfAccount = accountRepository.getAccount(transactionData);
        reqAccount = accountRepository.getAccount(reqAccount);

        reqAccount.setBalance(reqAccount.getBalance() - transactionData.getBalance());
        trfAccount.setBalance(trfAccount.getBalance() + transactionData.getBalance());

        reqAccount.getBalanceHistories().add(new BalanceHistory(transactionData.getAccountNumber(), reqAccount.getBalance(), 0, transactionData.getBalance()));
        trfAccount.getBalanceHistories().add(new BalanceHistory(reqAccount.getAccountNumber(), trfAccount.getBalance(), transactionData.getBalance(), 0));

        reqAccount = accountRepository.updateAccount(reqAccount);
        trfAccount = accountRepository.updateAccount(trfAccount);

        chkAccResult.setResult(reqAccount);
        chkAccResult.setNavigation(TO_WITHDRAWAL_SUMMARY.getValue());

        return chkAccResult;
    }

    private Account toAccount(TransferredAccount reqTrfAccount) {
        Account proceedTrfAccount = new Account();
        proceedTrfAccount.setBalance(Integer.parseInt(reqTrfAccount.getTransferredAmount()));
        proceedTrfAccount.setAccountNumber(reqTrfAccount.getAccountNumber());

        return proceedTrfAccount;
    }

    private Result<Account> validateInputBeforeTransfer(TransferredAccount trfAccount, Account account) {
        Result<Account> result = new Result<>(account, 0, "", TO_FUND_TRANSFER_SUMMARY.getValue(), VALID);

        if (!trfAccount.getAccountNumber().matches("[0-9]+")) {
            result.setMessage("Invalid account");
            result.setValid(INVALID);
        }
        Account reqTrfAccount = toAccount(trfAccount);
        Account existTrfAccount = accountRepository.getAccount(reqTrfAccount);
        if (VALID == result.getValid() && existTrfAccount == null) {
            result.setMessage("Invalid account");
            result.setValid(INVALID);
        }
        if (VALID == result.getValid() && !trfAccount.getTransferredAmount().matches("[0-9]+")) {
            result.setMessage("Invalid amount");
            result.setValid(INVALID);
        }

        int trfAmount = VALID == result.getValid()? Integer.parseInt(trfAccount.getTransferredAmount()): 0;
        if (VALID == result.getValid() && trfAmount < 1) {
            result.setMessage("Minimum amount to withdraw is $1");
            result.setValid(INVALID);
        }
        if (VALID == result.getValid() && trfAmount > 1000) {
            result.setMessage("Maximum amount to withdraw is $1000");
            result.setValid(INVALID);
        }
        Account existAccount = accountRepository.getAccount(account);
        if (VALID == result.getValid() &&  existAccount.getBalance() < trfAmount) {
            result.setMessage("Insufficient balance $" + existAccount.getBalance());
            result.setValid(INVALID);
        }

        if (INVALID == result.getValid()) {
            outputHelper.print(result.getMessage());
            result.setNavigation(TO_FUND_TRANSFER.getValue());
        }
        return result;
    }

    //
    public int confirmTransaction(TransferredAccount trfAccount, Account account) {
        final String text = String.format(
                "Transfer Confirmation\nDestination Account: %s\nTransfer Amount : %s\n Reference Number : %s\n%s\nChoose Options[2]",
                trfAccount.getAccountNumber(),
                trfAccount.getTransferredAmount(),
                trfAccount.getReferenceNumber(),
                menu.getFundTransferComfirmationOption().entrySet().stream()
                        .map(extractValue).collect(joining("")));

        String chooseOptInString = inputHelper.prompt(text);

        if (StringUtils.isBlank(chooseOptInString) || StringUtils.equals("2", chooseOptInString))
            chooseOptInString = String.valueOf(TO_FUND_TRANSFER.getValue());
        else if (StringUtils.equals("1", chooseOptInString))
            chooseOptInString = String.valueOf(TO_FUND_TRANSFER_CONFIRMATION.getValue());

        return Integer.parseInt(chooseOptInString);
    }
}
