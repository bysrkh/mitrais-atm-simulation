package com.github.bysrkh.mitraisatmsimulation.dto;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;

import java.util.HashMap;
import java.util.Map;

public class AccountRepository {

    private static Map<String, Account> accounts = new HashMap<>();

    static {
        accounts.put("112233", new Account("John Doe", "012108", 100, 0, "112233"));
        accounts.put("112244", new Account("Jane Doe", "932012", 30, 0, "112244"));
    }

    public static Map<String, Account> getAccounts() {
        return accounts;
    }

    public static void setAccounts(Map<String, Account> accounts) {
        AccountRepository.accounts = accounts;
    }
}
