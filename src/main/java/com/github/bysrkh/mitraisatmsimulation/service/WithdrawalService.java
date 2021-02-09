package com.github.bysrkh.mitraisatmsimulation.service;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import com.github.bysrkh.mitraisatmsimulation.dto.MenuDto;
import com.github.bysrkh.mitraisatmsimulation.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.github.bysrkh.mitraisatmsimulation.util.constant.WithdrawConstant.*;

@Service
public class WithdrawalService {
    private AccountRepository accountRepository;

    @Transactional
    public String withdrawFixedBalance(MenuDto menu) {
        Account account = accountRepository.findByAccountNumber(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new RuntimeException(""));
        Integer withdrawValue = 0;
        if (menu.getWithdrawalOption() == TEN_DOLLAR_OPTION) {
            withdrawValue = TEN_DOLLAR_VALUE;
        } else if (menu.getWithdrawalOption() == FIFTY_DOLLAR_OPTION) {
            withdrawValue = FIFTY_DOLLAR_VALUE;
        } else if (menu.getWithdrawalOption() == ONE_HUNDRED_DOLLAR_OPTION) {
            withdrawValue = ONE_HUNDRED_DOLLAR_VALUE;
        }

        account.setBalance(account.getBalance() - withdrawValue);

        return "Summary";
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
