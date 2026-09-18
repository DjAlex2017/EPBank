package com.epbank.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity 
@Table(name = "transactions")
public class Transaction {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount; 

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne 
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

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
    public Account getAccount(){
        return account;
    }
    public void setAccount(Account account){
        this.account = account;
    }

    @PrePersist 
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }
}
