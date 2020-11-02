package com.github.bysrkh.mitraisatmsimulation.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Account {
    private String name;
    private String pin;
    private int balance;
    private String accountNumber;
    private List<BalanceHistory> balanceHistories = new ArrayList<>();

    public Account() {
    }

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public Account(String name, String pin, int balance, String accountNumber) {
        this.name = name;
        this.pin = pin;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    public List<BalanceHistory> getBalanceHistories() {
        return balanceHistories;
    }

    public void setBalanceHistories(List<BalanceHistory> balanceHistories) {
        this.balanceHistories = balanceHistories;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            throw new RuntimeException("Object must be filled");

        if (!(obj instanceof Account))
            throw new RuntimeException("Object must be same data type");

        Account comparedAccount = (Account) obj;
        if (!(StringUtils.equals(this.getAccountNumber(), comparedAccount.getAccountNumber()) && StringUtils.equals(this.getPin(), comparedAccount.getPin())))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(123, this.getAccountNumber(), this.getBalance(), this.getName(), this.getPin());
    }
}
