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
import com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant;
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

    private List<NavigationConstant> acceptableNavigation = asList(
            TO_TRANSACTION,
            TO_FUND_TRANSFER,
            TO_FUND_TRANSFER_CONFIRMATION,
            TO_FUND_TRANSFER_SUMMARY,
            TO_WITHDRAWAL,
            TO_WITHDRAWAL_SUMMARY,
            TO_TRANSACTION_HISTORY,
            TO_OTHER_WITHDRAWAL
    );
    private List<NavigationConstant> nonAcceptableFixedWithdrawNavigation = asList(
            TO_TRANSACTION,
            TO_OTHER_WITHDRAWAL
    );
    private List<Integer> acceptableFixedWithdrawalNavigation = asList(1, 2, 3);

    @ShellMethod("Start to transaction: ")
    public String start() {
        Result<Account> result = new Result(new Account(), "", TO_TRANSACTION, VALID);
        do {
            result = accountService.loginAccount(result);
            result = proccessAtmScreen(result);
        } while (result.getNavigation() == TO_WELCOME_SCREEN);

        return "good bye";
    }

    private Result<Account> proccessAtmScreen(Result<Account> result) {
        Result<TransferredAccount> transferredAccountResult = new Result<TransferredAccount>(new TransferredAccount(), "", TO_TRANSACTION, VALID);
        while (acceptableNavigation.contains(result.getNavigation())) {
            if (TO_TRANSACTION == result.getNavigation()) {
                result.setNavigation(NavigationConstant.fromValue(menuService.choosenMenu()));

            } else if (result.getNavigation() == TO_WITHDRAWAL) {
                int choosenWithdraw = menuService.choosenWithdraw();
                result.setAdditionalNavigation(choosenWithdraw);
                result.setNavigation(NavigationConstant.fromValue(choosenWithdraw));
                if (acceptableFixedWithdrawalNavigation.contains(result.getAdditionalNavigation()))
                    result = withdrawalService.withdrawFixedBalance(result);

            } else if (result.getNavigation() == TO_OTHER_WITHDRAWAL) {
                result.setAdditionalNavigation(menuService.chooseWitdhrawalAdditionalOption());
                result = withdrawalService.withdrawOtherBalance(result);

            } else if (TO_FUND_TRANSFER == result.getNavigation()) {
                transferredAccountResult = fundTransferService.inputTransferedAccount();
                result.setNavigation(transferredAccountResult.getNavigation());

            } else if (result.getNavigation() == TO_WITHDRAWAL_SUMMARY) {
                result.setNavigation(NavigationConstant.fromValue(
                        menuService.chooseWithdrawSummary(result.getResult()))
                );

            } else if (TO_FUND_TRANSFER_CONFIRMATION == result.getNavigation()) {
                result.setNavigation(NavigationConstant.fromValue(
                        fundTransferService.confirmTransaction(transferredAccountResult.getResult(), result.getResult()))
                );
                if (TO_FUND_TRANSFER != result.getNavigation())
                    result = fundTransferService.transferAmount(transferredAccountResult.getResult(), result.getResult());

            } else if (TO_FUND_TRANSFER_SUMMARY == result.getNavigation()) {
                result.setNavigation(NavigationConstant.fromValue(
                        menuService.chooseFundTransferSummary(transferredAccountResult.getResult(), result.getResult()))
                );
            } else if (TO_TRANSACTION_HISTORY == result.getNavigation()) {
                result.setNavigation(NavigationConstant.fromValue(
                        transactionHistoryService.printTransactionHistory(result))
                );

            }
        }
        return result;
    }

}



