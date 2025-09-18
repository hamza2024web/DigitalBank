package com.hamza.digitalbank.repository;

import com.hamza.digitalbank.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    void save(Account account);
    Optional<Account>findById(String accountId);
    List<Account> findAll();
}
