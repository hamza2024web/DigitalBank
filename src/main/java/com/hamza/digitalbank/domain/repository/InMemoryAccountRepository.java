package com.hamza.digitalbank.domain.repository;

import com.hamza.digitalbank.domain.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryAccountRepository implements AccountRepository{
    private final Map<String,Account> accountStore = new HashMap<>();

    @Override
    public void save(Account account) {
        accountStore.put(account.getAccountId(),account);
    }

    @Override
    public Optional<Account> findById(String accountId) {
        return Optional.ofNullable(accountStore.get(accountId));
    }
}
