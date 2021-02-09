package com.github.bysrkh.mitraisatmsimulation.dto;

public class MenuDto {
    private Integer transactionOption;
    private Integer withdrawalOption;

    public Integer getTransactionOption() {
        return transactionOption;
    }

    public void setTransactionOption(Integer transactionOption) {
        this.transactionOption = transactionOption;
    }

    public Integer getWithdrawalOption() {
        return withdrawalOption;
    }

    public void setWithdrawalOption(Integer withdrawalOption) {
        this.withdrawalOption = withdrawalOption;
    }
}
