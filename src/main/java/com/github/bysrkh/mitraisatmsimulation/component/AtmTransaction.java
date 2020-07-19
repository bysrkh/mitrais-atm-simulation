package com.github.bysrkh.mitraisatmsimulation.component;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;
import com.github.bysrkh.mitraisatmsimulation.repository.AccountRepository;
import com.github.bysrkh.mitraisatmsimulation.service.AccountService;
import com.github.bysrkh.mitraisatmsimulation.service.FundTransferService;
import com.github.bysrkh.mitraisatmsimulation.service.MenuService;
import com.github.bysrkh.mitraisatmsimulation.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import static com.github.bysrkh.mitraisatmsimulation.NavigationConstant.*;

@ShellComponent
public class AtmTransaction {

    @Autowired
    private MenuService menuService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private FundTransferService fundTransferService;
    @Autowired
    private OutputHelper outputHelper;
    @Autowired
    private TransactionHistoryService transactionHistoryService;
    @Autowired
    private AccountRepository accountRepository;

    @ShellMethod("Start to transaction: ")
    public String start() {
        Result<Account> result = new Result<>(new Account(), 1, "");

        do {
            accountService.inputAccount(result);
            if (result.getChoose() != 1) {
                outputHelper.print(result.getMessage());
                continue;
            }
            result.setChoose(-1);
            while (result.getChoose() == -1) {
                result.setChoose(menuService.choosenMenu());
                if (result.getChoose() == 1) {
                    do {
                        result.setChoose(menuService.choosenWithdraw());

                        if (result.getChoose() <= 3) {
                            switch (result.getChoose()) {
                                case 1:
                                    result.getResult().setDeductedBalance(10);
                                    break;
                                case 2:
                                    result.getResult().setDeductedBalance(50);
                                    break;
                                case 3:
                                    result.getResult().setDeductedBalance(100);
                                    break;
                                default:
                                    break;
                            }
                            accountService.deductAccount(result);
                            if (result.getChoose() == INVALID) {
                                result.setChoose(TO_WITDHRAWAL_OPTION);
                                continue;
                            }
                            result.setChoose(menuService.chooseSummary(result.getResult()));
                            if (result.getChoose() == 1) {
                                result.setChoose(TO_TRANSACTION_OPTION);
                            } else if (result.getChoose() == 2) {
                                result.setChoose(TO_WELCOME);
                            }
                            continue;
                        } else if (result.getChoose() == 4) {
                            do {
                                result.getResult().setDeductedBalance(menuService.chooseWitdhrawalAdditionalOption());
                                accountService.deductAccount(result);
                            } while (result.getChoose() == INVALID);

                            result.setChoose(menuService.chooseSummary(result.getResult()));
                            if (result.getChoose() == 1)
                                result.setChoose(TO_TRANSACTION_OPTION);
                            else
                                result.setChoose(TO_WELCOME);
                        } else if (result.getChoose() == 5) {
                            result.setChoose(TO_TRANSACTION_OPTION);
                            continue;
                        }
                    } while (result.getChoose() == -2);

                } else if (result.getChoose() == 2) {
                    do {
                        fundTransferService.inputTransferedAccount(result);
                        if (result.getChoose() == INVALID) {
                            result.setChoose(TO_TRANSACTION_OPTION);
                            break;
                        }
                        fundTransferService.isValidData(result);
                        if (result.getChoose() == INVALID) {
                            outputHelper.print(result.getMessage());
                            continue;
                        }
                        fundTransferService.confirmTransaction(result);
                        if (result.getChoose() == INVALID) {
                            continue;
                        }
                        fundTransferService.transferAmount(result);
                    } while (result.getChoose() == 0);
                    if (result.getChoose() == -1)
                        continue;
                    result.setChoose(menuService.chooseFundTransferSummary(result.getResult().getTransferredAccount(), result.getResult()));
                    if (result.getChoose() == 1) {
                        result.setChoose(-1);
                    } else {
                        result.setChoose(0);
                    }

                } else if (result.getChoose() == 3) {
                    transactionHistoryService.printTransactionHistory(result);
                    result.setChoose(TO_TRANSACTION_OPTION);
                }
            }

        } while (result.getChoose() == 0);
//
//        while (choosen != menuService.choosenMenu()) {
//            if (choosen == 1) {
//                choosen = menuService.choosenWithdraw();
//                if (choosen <= 3) {
//                    Result<Account> result = accountService.deductAccount(null);
//                    if (result.getChoose() == 0) System.exit(0);
//                }
//            }
//
//            choosen = 3;
//        }

        try {
            accountRepository.saveAccountsToCsv();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return "goodbye";
    }
}





