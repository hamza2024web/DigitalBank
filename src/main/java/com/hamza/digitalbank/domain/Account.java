package com.hamza.digitalbank.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Account {
    private String accountId;
    private UUID ownerUserId;
    private BigDecimal balance;
    private Instant createdAt;
    private boolean active;

    public Account(String accountId , UUID ownerUserId){
        this.accountId = accountId;
        this.ownerUserId = ownerUserId;
        this.balance = BigDecimal.ZERO;
        this.createdAt = Instant.now();
        this.active = true;
    }

    public String getAccountId(){
        return accountId;
    }

    public UUID getOwnerUserId(){
        return ownerUserId;
    }

    public BigDecimal getBalance(){
        return balance;
    }

    public Instant getInstant(){
        return createdAt;
    }

    public Boolean isActive(){
        return active;
    }

    public void setBalance(BigDecimal balance){
        this.balance = balance;
    }

    public void setActive(Boolean active){
        this.active = active;
    }
}
