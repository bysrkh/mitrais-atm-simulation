package com.github.bysrkh.mitraisatmsimulation.component;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.domain.Menu;
import com.github.bysrkh.mitraisatmsimulation.domain.TransferredAccount;
import com.github.bysrkh.mitraisatmsimulation.dto.Result;
import com.github.bysrkh.mitraisatmsimulation.service.AccountService;
import com.github.bysrkh.mitraisatmsimulation.service.FundTransferService;
import com.github.bysrkh.mitraisatmsimulation.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;

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
    @ShellMethod("Start to transaction: ")
    public String start() {
        Result<Account> result = new Result<>(new Account(), 0, "");

        do {
            result = accountService.inputAccount();
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
                                case 1: result.getResult().setDeductedBalance(10); break;
                                case 2: result.getResult().setDeductedBalance(50); break;
                                case 3: result.getResult().setDeductedBalance(100); break;
                                default: break;
                            }
                            result = accountService.deductAccount(result.getResult());
                            if (result.getChoose() == 0) {
                                result.setChoose(-2);
                                continue;
                            }
                            result.setChoose(menuService.chooseSummary(result.getResult()));
                            if (result.getChoose() == 1) {
                                result.setChoose(-1);
                            } else if (result.getChoose() == 2) {
                                result.setChoose(0);
                            }
                            continue;
                        } else if (result.getChoose() == 4) {
                            do {
                                result.getResult().setDeductedBalance(menuService.chooseWitdhrawalAdditionalOption());
                                result = accountService.deductAccount(result.getResult());
                            } while (result.getChoose() == 0);

                            result.setChoose(menuService.chooseSummary(result.getResult()));
                            if (result.getChoose() == 1)
                                result.setChoose(-1);
                            else
                                result.setChoose(0);
                        } else if (result.getChoose() == 5) {
                            result.setChoose(-1);
                            continue;
                        }
                    } while (result.getChoose() == -2);

                } else if (result.getChoose() == 2){
                    do {
                        result = fundTransferService.inputTransferedAccount(result.getResult());
                        if (result.getChoose() == -1) {
                            break;
                        }
                        result = fundTransferService.isValidData(result.getResult());
                        if (result.getChoose() == 0) {
                            outputHelper.print(result.getMessage());
                            continue;
                        }
                        result = fundTransferService.confirmTransaction(result.getResult());
                        if (result.getChoose() == 2) {
                            result.setChoose(0);
                            continue;
                        }
                        result = fundTransferService.transferAmount(result.getResult());
                    } while (result.getChoose() == 0);
                        if (result.getChoose() == -1)
                            continue;
                        result.setChoose(menuService.chooseFundTransferSummary(result.getResult().getTransferredAccount(), result.getResult()));
                        if (result.getChoose() == 1) {
                            result.setChoose(-1);
                        } else {
                            result.setChoose(0);
                        }

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

        return "goodbye";
    }
}





