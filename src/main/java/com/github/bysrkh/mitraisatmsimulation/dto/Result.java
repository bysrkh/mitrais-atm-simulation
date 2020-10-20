package com.github.bysrkh.mitraisatmsimulation.dto;

public class  Result<T> {
    private T result;
    private int operation;
    private String message;
    private int navigation;
    private int valid;

    public Result(T result, int choose, String message, int navigation, int valid) {
        this.result = result;
        this.operation = choose;
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

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }
}
