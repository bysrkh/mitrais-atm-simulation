package com.github.bysrkh.mitraisatmsimulation.util.constant;

public enum NavigationConstant {
    TO_WITHDRAWAL(1),
    TO_FUND_TRANSFER(2),
    TO_TRANSACTION_HISTORY(3),
    TO_OTHER_WITHDRAWAL(4),
    TO_WELCOME_SCREEN(101),
    TO_TRANSACTION(102),
    TO_WITHDRAWAL_SUMMARY(103),
    TO_FUND_TRANSFER_CONFIRMATION(104),
    TO_FUND_TRANSFER_SUMMARY(105),
    ;

    private int value;

    NavigationConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static NavigationConstant fromValue(int value) {
        for (NavigationConstant navCt : values()) {
            if (navCt.getValue() == value) return navCt;
        }

        throw new RuntimeException("Enum is not listed");
    }
}
