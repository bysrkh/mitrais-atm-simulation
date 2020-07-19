package com.github.bysrkh.mitraisatmsimulation.domain;

public class BalanceHistory {
    private String accountNumber;
    private int currentBalance;
    private int creditedBalance;
    private int debitedBalance;

    public BalanceHistory() {
    }

    public BalanceHistory(String accountNumber, int currentBalance, int creditedBalance, int debitedBalance) {
        this.accountNumber = accountNumber;
        this.currentBalance = currentBalance;
        this.creditedBalance = creditedBalance;
        this.debitedBalance = debitedBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(int currentBalance) {
        this.currentBalance = currentBalance;
    }

    public int getCreditedBalance() {
        return creditedBalance;
    }

    public void setCreditedBalance(int creditedBalance) {
        this.creditedBalance = creditedBalance;
    }

    public int getDebitedBalance() {
        return debitedBalance;
    }

    public void setDebitedBalance(int debitedBalance) {
        this.debitedBalance = debitedBalance;
    }
}
