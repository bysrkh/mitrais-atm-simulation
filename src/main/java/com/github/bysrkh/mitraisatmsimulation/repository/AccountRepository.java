package com.github.bysrkh.mitraisatmsimulation.repository;

import com.github.bysrkh.mitraisatmsimulation.component.OutputHelper;
import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.domain.BalanceHistory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AccountRepository {
    private Map<String, Account> accounts = new HashMap<>();

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private OutputHelper outputHelper;

    Function<String, List<BalanceHistory>> convertToList = balanceHistory -> {
        List<BalanceHistory> balanceHistories = new ArrayList<>();
        if (StringUtils.isBlank(balanceHistory))
            return balanceHistories;

        String[] balanceRecord = balanceHistory.split(";");

        for (int i = 0; i < balanceRecord.length; i++) {
            String[] balanceItems = balanceRecord[i].split("|");
            balanceHistories.add(new BalanceHistory(balanceItems[0], Integer.parseInt(balanceItems[1]), Integer.parseInt(balanceItems[2]), Integer.parseInt(balanceItems[3])));
        }
        return balanceHistories;
    };

    public Account updateAccount(Account account) {
        return accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(Account account) {

        Account existingAccount = getAccountFromMap(account);
        if (existingAccount == null) {
            throw new RuntimeException("Account is not found");
        }
        return existingAccount;
    }

    private Account getAccountFromMap(Account account) {
        return accounts.get(account.getAccountNumber());
    }

    public Map<String, Account> saveAll(Map<String, Account> accounts) {
        return null;
    }

    @PostConstruct
    public void prepareAccountsFromCsv() {
        CSVParser records = null;
        try {
            InputStreamReader fileResource = new InputStreamReader(resourceLoader.getResource("classpath:csv/data.csv").getInputStream());
            records = CSVFormat.DEFAULT.withHeader("Name", "PIN", "Balance", "Account Number", "Balance History").withTrim().withFirstRecordAsHeader().parse(fileResource);
        } catch (IOException exc) {
            outputHelper.print(exc.getMessage());
            exc.printStackTrace();
        }

        records.forEach(record -> {
            Account account = new Account();
            account.setName(record.get("Name"));
            account.setPin(record.get("PIN"));
            account.setBalance(Integer.parseInt(record.get("Balance")));
            account.setAccountNumber(record.get("Account Number"));
            account.setBalanceHistories(convertToList.apply(record.get("Balance History")));

            accounts.put(account.getAccountNumber(), account);
        });

        System.out.print("Winda Nurmala");
        System.out.println(accounts.get("112233").getBalance());
    }

    public void saveAccountsToCsv() throws Exception {
        FileWriter out = new FileWriter("D:/data.csv");
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                .withHeader("Name", "PIN", "Balance", "Account Number", "Balance History"))) {
            accounts.values().forEach(account -> {
                try {
                    printer.printRecord(
                            account.getName(),
                            account.getPin(),
                            account.getBalance(),
                            account.getAccountNumber(),
                            account.getBalanceHistories().stream().map(balanceHistory -> String.format("%s|%s|%s|%s", balanceHistory.getAccountNumber(), balanceHistory.getAccountNumber(), balanceHistory.getCreditedBalance(), balanceHistory.getDebitedBalance()))
                                    .collect(Collectors.joining(";"))
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
