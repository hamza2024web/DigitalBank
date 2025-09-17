package com.hamza.digitalbank.repository;

import com.hamza.digitalbank.domain.Transaction;

import java.util.*;

public class InMemoryTransactionRepository implements TransactionRepository{
    private final Map<String, List<Transaction>> transactionStore = new HashMap<>();

    @Override
    public void save(Transaction transaction) {
        List<Transaction> transactions = transactionStore.computeIfAbsent(transaction.getAccountId(), k -> new ArrayList<>());

        transactions.add(transaction);
    }

    @Override
    public List<Transaction> findAllByAccountId(String accountId) {
        return transactionStore.getOrDefault(accountId, Collections.emptyList());
    }
}
