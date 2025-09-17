package com.hamza.digitalbank.repository;

import com.hamza.digitalbank.domain.Account;

import java.util.Optional;

public interface AccountRepository {
    void save(Account account);
    Optional<Account>findById(String accountId);
}
