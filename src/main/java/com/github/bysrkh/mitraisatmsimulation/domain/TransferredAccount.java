package com.github.bysrkh.mitraisatmsimulation.domain;

public class TransferredAccount {
    private String accountNumber;
    private String transferredAmount;
    private String transferredAmountInString;
    private String referenceNumber;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransferredAmount() {
        return transferredAmount;
    }

    public void setTransferredAmount(String transferredAmount) {
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
