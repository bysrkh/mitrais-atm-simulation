package com.github.bysrkh.mitraisatmsimulation.dto;

public class Result<T> {
    private T result;
    private int choose;
    private String message;

    public Result(T result, int choose, String message) {
        this.result = result;
        this.choose = choose;
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getChoose() {
        return choose;
    }

    public void setChoose(int choose) {
        this.choose = choose;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
