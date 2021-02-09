package com.github.bysrkh.mitraisatmsimulation.repository;

import com.github.bysrkh.mitraisatmsimulation.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByAccountNumber(String accountNumber);
}
