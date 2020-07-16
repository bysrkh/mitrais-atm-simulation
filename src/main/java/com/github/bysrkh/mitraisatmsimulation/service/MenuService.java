package com.github.bysrkh.mitraisatmsimulation.service;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.domain.TransferredAccount;

public interface MenuService {
    int choosenMenu();

    int choosenWithdraw();

    int chooseSummary(Account account);

    int chooseWitdhrawalAdditionalOption();

    int chooseFundTransferSummary(TransferredAccount transferredAccount, Account account);
}
