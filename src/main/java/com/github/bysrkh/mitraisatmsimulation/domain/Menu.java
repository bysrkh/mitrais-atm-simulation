package com.github.bysrkh.mitraisatmsimulation.domain;

import java.util.HashMap;
import java.util.Map;

public class Menu {
    private Map<Integer, String> transactionOption = new HashMap<>();
    private Map<Integer, String> withdrawalOption = new HashMap<>();
    private Map<Integer, String> fundTransferOption = new HashMap<>();
    private Map<Integer, String> fundTransferComfirmationOption = new HashMap<>();
    private Map<Integer, String> summaryOptionOption = new HashMap<>();

    public Menu() {
        this.transactionOption.put(1, "Withdraw");
        this.transactionOption.put(2, "Fund Transfer");
        this.transactionOption.put(3, "Exit");

        this.withdrawalOption.put(1, "$10");
        this.withdrawalOption.put(2, "$50");
        this.withdrawalOption.put(3, "$100");
        this.withdrawalOption.put(4, "Other");
        this.withdrawalOption.put(5, "Back");

        this.fundTransferOption.put(1, "Destination Account");
        this.fundTransferOption.put(2, "Transfer Amount");
        this.fundTransferOption.put(3, "Reference Number");

        this.fundTransferComfirmationOption.put(1, "Confirm Trx");
        this.fundTransferComfirmationOption.put(2, "Cancel Trx");

        this.summaryOptionOption.put(1, "Transaction");
        this.summaryOptionOption.put(2, "Exit");
    }

    public Map<Integer, String> getTransactionOption() {
        return transactionOption;
    }

    public Map<Integer, String> getWithdrawalOption() {
        return withdrawalOption;
    }

    public Map<Integer, String> getFundTransferOption() {
        return fundTransferOption;
    }

    public Map<Integer, String> getFundTransferComfirmationOption() {
        return fundTransferComfirmationOption;
    }

    public Map<Integer, String> getSummaryOptionOption() {
        return summaryOptionOption;
    }
}
