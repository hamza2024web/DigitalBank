package com.hamza.digitalbank.repository;

import com.hamza.digitalbank.domain.Account;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public List<Account> findAllByOwnerUserId(UUID ownerUserId) {
        return accountStore.values().stream().filter(account -> account.getOwnerUserId().equals(ownerUserId)).collect(Collectors.toList());
    }

}
