package com.hamza.digitalbank.domain.repository;

import com.hamza.digitalbank.domain.Transaction;

import java.util.List;

public interface TransactionRepository {
    void save(Transaction transaction);
    List<Transaction> findAllByAccountId(String accountId);
}
