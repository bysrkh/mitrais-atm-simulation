package com.github.bysrkh.mitraisatmsimulation.util.constant;

public enum ValidConstant {
    INVALID(0),
    VALID(1);

    private int value;

    private ValidConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
