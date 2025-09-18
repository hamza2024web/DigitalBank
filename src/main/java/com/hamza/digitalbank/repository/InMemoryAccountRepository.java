package com.hamza.digitalbank.repository;

import com.hamza.digitalbank.domain.Account;

import java.util.*;

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

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accountStore.values());
    }
}
