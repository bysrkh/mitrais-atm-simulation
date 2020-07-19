package com.github.bysrkh.mitraisatmsimulation.repository;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;

import java.util.Map;


public interface AccountRepository {

    Account updateAccount(Account account);
    Account getAccount(Account account);
    Map<String, Account> saveAll(Map<String, Account> accounts);
    public void saveAccountsToCsv() throws Exception;
}
