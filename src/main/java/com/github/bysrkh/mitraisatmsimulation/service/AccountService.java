package com.github.bysrkh.mitraisatmsimulation.service;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.repository.AccountRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private ResourceLoader resourceLoader;
    private AccountRepository accountRepository;

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void prepareAccountsFromCsv() {
        CSVParser records = null;
        try {
            InputStreamReader fileResource = new InputStreamReader(resourceLoader.getResource("classpath:csv/data.csv").getInputStream());
            records = CSVFormat.DEFAULT.withHeader("Name", "PIN", "Balance", "Account Number", "Id").withTrim().withFirstRecordAsHeader().parse(fileResource);
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        try {
            List<Account> accountList = records.getRecords().stream().map(record -> {
                Account account = new Account();
                account.setId(record.get("Id"));
                account.setName(record.get("Name"));
                account.setPin(record.get("PIN"));
                account.setBalance(Integer.parseInt(record.get("Balance")));
                account.setAccountNumber(record.get("Account Number"));

                return account;
            }).collect(Collectors.toList());
            accountRepository.saveAll(accountList);
        } catch (IOException exc) {
            exc.printStackTrace();
        }

    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}

