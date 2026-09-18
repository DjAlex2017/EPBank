package com.epbank.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.epbank.backend.model.TransactionType;

public class TransactionResponse {
    private Long id;
    private TransactionType transactionType;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private Long accountId;

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public TransactionType getTransactionType(){
        return transactionType;
    }
    public void setTransactionType(TransactionType transactionType){
        this.transactionType = transactionType;
    }
    public BigDecimal getAmount(){
        return amount;
    }
    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
    public Long getAccountId(){
        return accountId;
    }
    public void setAccountId(Long accountId){
        this.accountId = accountId;
    }
}
