package com.github.bysrkh.mitraisatmsimulation.component;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.domain.TransferredAccount;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;
import com.github.bysrkh.mitraisatmsimulation.repository.AccountRepository;
import com.github.bysrkh.mitraisatmsimulation.service.AccountService;
import com.github.bysrkh.mitraisatmsimulation.service.FundTransferService;
import com.github.bysrkh.mitraisatmsimulation.service.MenuService;
import com.github.bysrkh.mitraisatmsimulation.service.TransactionHistoryService;
import com.github.bysrkh.mitraisatmsimulation.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_FUND_TRANSFER;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_FUND_TRANSFER_CONFIRMATION;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_FUND_TRANSFER_SUMMARY;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_OTHER_WITHDRAWAL;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_TRANSACTION;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_TRANSACTION_HISTORY;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_WELCOME_SCREEN;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_WITHDRAWAL;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant.TO_WITHDRAWAL_SUMMARY;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.ValidConstant.INVALID;
import static com.github.bysrkh.mitraisatmsimulation.util.constant.ValidConstant.VALID;
import static java.util.Arrays.asList;

@ShellComponent
public class AtmTransaction {

    @Autowired
    private MenuService menuService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private FundTransferService fundTransferService;
    @Autowired
    private WithdrawalService withdrawalService;
    @Autowired
    private OutputHelper outputHelper;
    @Autowired
    private TransactionHistoryService transactionHistoryService;
    @Autowired
    private AccountRepository accountRepository;

    private List<Integer> acceptableNavigation = asList(
            TO_TRANSACTION.getValue(),
            TO_FUND_TRANSFER.getValue(),
            TO_FUND_TRANSFER_CONFIRMATION.getValue(),
            TO_FUND_TRANSFER_SUMMARY.getValue(),
            TO_WITHDRAWAL.getValue(),
            TO_WITHDRAWAL_SUMMARY.getValue(),
            TO_TRANSACTION_HISTORY.getValue(),
            TO_OTHER_WITHDRAWAL.getValue()
    );

    @ShellMethod("Start to transaction: ")
    public String start() {
        Result<Account> result = new Result(new Account(), 0, "", TO_TRANSACTION.getValue(), VALID.getValue());
        Result<TransferredAccount> transferredAccountResult = new Result<>(new TransferredAccount(), 0, "", TO_TRANSACTION.getValue(), VALID.getValue());

        do {
            result = accountService.inputAccount(result);

            while (acceptableNavigation.contains(result.getNavigation())) {
                if (TO_TRANSACTION.getValue() == result.getNavigation()) {
                    result.setNavigation(menuService.choosenMenu());

                } else if (result.getNavigation() == TO_WITHDRAWAL.getValue()) {
                    result.setOperation(menuService.choosenWithdraw());
                    if (TO_OTHER_WITHDRAWAL.getValue() == result.getOperation() || TO_TRANSACTION.getValue() == result.getOperation()) {
                        result.setNavigation(result.getOperation());
                        result.setOperation(0);

                        continue;
                    }
                    result = withdrawalService.withdrawFixedBalance(result);

                } else if (result.getNavigation() == TO_OTHER_WITHDRAWAL.getValue()) {
                    result.setOperation(menuService.chooseWitdhrawalAdditionalOption());
                    result = withdrawalService.withdrawOtherBalance(result);

                } else if (TO_FUND_TRANSFER.getValue() == result.getNavigation()) {
                    transferredAccountResult = fundTransferService.inputTransferedAccount();
                    result.setNavigation(transferredAccountResult.getNavigation());

                } else if (result.getNavigation() == TO_WITHDRAWAL_SUMMARY.getValue()) {
                    result.setNavigation(menuService.chooseWithdrawSummary(result.getResult()));

                } else if (TO_FUND_TRANSFER_CONFIRMATION.getValue() == result.getNavigation()) {
                    result.setNavigation(fundTransferService.confirmTransaction(transferredAccountResult.getResult(), result.getResult()));

                    if (TO_FUND_TRANSFER.getValue() == result.getNavigation()) continue;

                    result = fundTransferService.transferAmount(transferredAccountResult.getResult(), result.getResult());

                } else if (TO_FUND_TRANSFER_SUMMARY.getValue() == result.getNavigation()) {
                    result.setNavigation(menuService.chooseFundTransferSummary(transferredAccountResult.getResult(), result.getResult()));
                } else if (TO_TRANSACTION_HISTORY.getValue() == result.getNavigation()) {
                    result.setNavigation(transactionHistoryService.printTransactionHistory(result));
                }
            }
        } while (result.getNavigation() == TO_WELCOME_SCREEN.getValue());

        return "good bye";
    }
}



