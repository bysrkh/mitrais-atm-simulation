package com.github.bysrkh.mitraisatmsimulation.dto;

import com.github.bysrkh.mitraisatmsimulation.util.constant.ValidConstant;

public class  Result<T> {
    private T result;
    private int operation;
    private String message;
    private int navigation;
    private ValidConstant valid;

    public Result(T result, int operation, String message, int navigation, ValidConstant valid) {
        this.result = result;
        this.operation = operation;
        this.message = message;
        this.navigation = navigation;
        this.valid = valid;
    }

    public Result() {
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int choose) {
        this.operation = choose;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNavigation() {
        return navigation;
    }

    public void setNavigation(int navigation) {
        this.navigation = navigation;
    }

    public ValidConstant getValid() {
        return valid;
    }

    public void setValid(ValidConstant valid) {
        this.valid = valid;
    }
}
