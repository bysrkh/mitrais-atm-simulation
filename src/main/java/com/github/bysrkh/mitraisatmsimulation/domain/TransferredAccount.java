package com.github.bysrkh.mitraisatmsimulation.domain;

public class TransferredAccount {
    private String accountNumber;
    private int transferredAmount;
    private String transferredAmountInString;
    private String referenceNumber;

    public TransferredAccount() {
    }

    public TransferredAccount(String accountNumber, int transferredAmount, String transferredAmountInString, String referenceNumber) {
        this.accountNumber = accountNumber;
        this.transferredAmount = transferredAmount;
        this.transferredAmountInString = transferredAmountInString;
        this.referenceNumber = referenceNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getTransferredAmount() {
        return transferredAmount;
    }

    public void setTransferredAmount(int transferredAmount) {
        this.transferredAmount = transferredAmount;
    }

    public String getTransferredAmountInString() {
        return transferredAmountInString;
    }

    public void setTransferredAmountInString(String transferredAmountInString) {
        this.transferredAmountInString = transferredAmountInString;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
}
