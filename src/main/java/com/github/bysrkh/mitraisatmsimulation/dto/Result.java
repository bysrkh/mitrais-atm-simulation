package com.github.bysrkh.mitraisatmsimulation.dto;

import com.github.bysrkh.mitraisatmsimulation.util.constant.NavigationConstant;
import com.github.bysrkh.mitraisatmsimulation.util.constant.ValidConstant;

public class  Result<T> {
    private T result;
    private String message;
    private NavigationConstant navigation;
    private int additionalNavigation;
    private ValidConstant valid;

    public Result(T result, String message, NavigationConstant navigation, ValidConstant valid) {
        this.result = result;
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


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NavigationConstant getNavigation() {
        return navigation;
    }

    public void setNavigation(NavigationConstant navigation) {
        this.navigation = navigation;
    }

    public int getAdditionalNavigation() {
        return additionalNavigation;
    }

    public void setAdditionalNavigation(int additionalNavigation) {
        this.additionalNavigation = additionalNavigation;
    }

    public ValidConstant getValid() {
        return valid;
    }

    public void setValid(ValidConstant valid) {
        this.valid = valid;
    }
}
