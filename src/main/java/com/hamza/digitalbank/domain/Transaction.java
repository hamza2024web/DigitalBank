package com.hamza.digitalbank.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private Instant timestamp;
    private String accountId;
    private TransactionType type;
    private BigDecimal amount;
    private String counterpartyAccountId;
    private String description;

    public Transaction (String accountId, TransactionType type, BigDecimal amount, String counterpartyAccountId, String description){
        this.id = UUID.randomUUID();
        this.timestamp = Instant.now();
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.counterpartyAccountId = counterpartyAccountId;
        this.description = description;
    }

    public UUID getId(){
        return id;
    }

    public Instant getTimestamp(){
        return timestamp;
    }

    public String getAccountId(){
        return accountId;
    }

    public BigDecimal getAmount(){
        return amount;
    }

    public TransactionType getType(){
        return type;
    }
    public String getCounterpartyAccountId(){
        return counterpartyAccountId;
    }

    public String getDescription(){
        return description;
    }
}
