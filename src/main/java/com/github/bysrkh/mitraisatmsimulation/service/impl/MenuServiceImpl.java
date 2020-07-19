package com.github.bysrkh.mitraisatmsimulation.service.impl;

import com.github.bysrkh.mitraisatmsimulation.component.InputHelper;
import com.github.bysrkh.mitraisatmsimulation.component.OutputHelper;
import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.domain.Menu;
import com.github.bysrkh.mitraisatmsimulation.domain.TransferredAccount;
import com.github.bysrkh.mitraisatmsimulation.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private InputHelper inputHelper;

    @Autowired
    private OutputHelper outputHelper;

    private Function<Map.Entry, String> extractValue = values -> String.format("%s. %s\n", values.getKey(), values.getValue());

    private final Menu menu = new Menu();

    @Override
    public int choosenMenu() {
        String choosenOptionInString;
            final String menuOption = String.format(
                    "%s \n %s",
                    menu.getTransactionOption().entrySet().stream()
                            .map(extractValue).collect(joining("")),
                    "Please choose option");
            choosenOptionInString = inputHelper.prompt(menuOption);
            if(StringUtils.isBlank(choosenOptionInString))
                choosenOptionInString = "4";

        return Integer.parseInt(choosenOptionInString);
    }

    @Override
    public int choosenWithdraw() {
        String choosenOptionInString;
            final String menuOption = String.format(
                    "%s \n %s",
                    menu.getWithdrawalOption().entrySet().stream()
                            .map(extractValue).collect(joining("")),
                    "Please choose option");
            choosenOptionInString = inputHelper.prompt(menuOption);
            if (StringUtils.isBlank(choosenOptionInString))
                choosenOptionInString = "5";

        return Integer.parseInt(choosenOptionInString);
    }

    @Override
    public int chooseWitdhrawalAdditionalOption() {
        String choosenOptionInString;
        int choosenOption = 0;
        do {
            final String menuOption = String.format(
                    "%s\n%s",
                    "Other withdraw",
                    "Enter amount to withdraw");
            choosenOptionInString = inputHelper.prompt(menuOption);
            if (StringUtils.isNotBlank(choosenOptionInString) && (!choosenOptionInString.matches("[0-9]+") || Integer.parseInt(choosenOptionInString) % 10 != 0)) {
                outputHelper.print("Invalid amount");
                choosenOptionInString = StringUtils.EMPTY;
            } else {
                choosenOption = Integer.parseInt(choosenOptionInString);
            }

        } while (StringUtils.isBlank(choosenOptionInString));

        return choosenOption;
    }

    @Override
    public int chooseSummary(Account account) {
        String choosenOptionInString;
        do {
            final String menuOption = String.format(
                    "Summary\n" +
                    "Date : %s\n" +
                    "Withdraw : $%s\n" +
                    "Balance : $%s \n" +
                            "%s",
                    new Date(), account.getDeductedBalance(), account.getBalance(),
                    menu.getSummaryOptionOption().entrySet().stream()
                            .map(extractValue).collect(joining("")),
                    "Please choose option");
            choosenOptionInString = inputHelper.prompt(menuOption);
        } while (StringUtils.isBlank(choosenOptionInString));

        return Integer.parseInt(choosenOptionInString);
    }

    @Override
    public int chooseFundTransferSummary(TransferredAccount transferredAccount, Account account) {
        String choosenOptionInString;
        do {
            final String menuOption = String.format(
                    "Summary\n" +
                            "Destination Account : %s\n" +
                            "Transfer Amount : $%s\n" +
                            "Reference Number : $%s \n" +
                            "Balance: %s\n" +
                            "%s",
                    transferredAccount.getAccountNumber(),
                    transferredAccount.getTransferredAmount(),
                    transferredAccount.getReferenceNumber(),
                    account.getBalance(),
                    menu.getSummaryOptionOption().entrySet().stream()
                            .map(extractValue).collect(joining("")),
                    "Please choose option");
            choosenOptionInString = inputHelper.prompt(menuOption);
        } while (StringUtils.isBlank(choosenOptionInString));

        return Integer.parseInt(choosenOptionInString);
    }
}
