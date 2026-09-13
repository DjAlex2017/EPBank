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

/* Annotation Definitions
@Entity 
- Tells JPA that this class represents an entity that can be stored in the database.

@Table(name = "accounts") 
- Maps this Account class to the "accounts" table in PostgreSQL.

@Id 
- Marks this field as the primary key of the database table.

@GeneratedValue(strategy = GenerationType.IDENTITY) 
- Tells JPA that the database is responsible for automatically generating the primary key value
when a new account is inserted.

@Column 
- Maps a Java field to a column in the database table.

@Column(name = "account_number", nullable = false, unique = true)
- Maps accountNumber to the "account_number" database column.
- nullable = false means the database does not allow this value to be NULL.
- unique = true means no two accounts can have the same account number.

@Enumerated(EnumType.STRING)
- Tells JPA to store an enum value as text in the database instead of as a number.
- For AccountType, values are stored as CHECKING or SAVINGS.
- For AccountStatus, values are stored as ACTIVE or CLOSED.

@Column(name = "account_type", nullable = false)
- Maps accountType to the "account_type" database column.
- nullable = false means the account must have an account type.

@Column(name = "balance", nullable = false)
- Maps balance to the "balance" database column.
- nullable = false means every account must have a balance value.

@Column(name = "status", nullable = false)
- Maps status to the "status" database column.
- nullable = false means every account must have a status.

@Column(name = "created_at", nullable = false)
- Maps createdAt to the "created_at" database column.
- nullable = false means every account must have a creation timestamp.

@ManyToOne
- Defines a relationship where many Account records can belong to one Customer.
- This allows one customer to have multiple bank accounts.

@JoinColumn(name = "customer_id", nullable = false)
- Creates the "customer_id" foreign key column in the accounts table.
- The customer_id connects an Account to the Customer that owns it.
- nullable = false means every account must belong to a customer.

@PrePersist
- Tells JPA to run this method automatically before a new Account
is inserted into the database.
- In this class, it sets createdAt to the current time.
- It also gives a new account a balance of 0 if no balance was set.
- It sets the account status to ACTIVE if no status was set.
*/

@Entity 
@Table(name = "accounts")
public class Account {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getAccountNumber(){
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber){
        this.accountNumber = accountNumber;
    }
  
    public AccountType getAccountType(){
        return accountType;
    }
    public void setAccountType(AccountType accountType){
        this.accountType = accountType;
    }
    public BigDecimal getBalance(){
        return balance;
    }
    public void setBalance(BigDecimal balance){
        this.balance = balance;
    }
    public AccountStatus getStatus(){
        return status;
    }
    public void setStatus(AccountStatus status){
        this.status = status;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
    public Customer getCustomer(){
        return customer;
    }
    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    @PrePersist 
    protected void onCreate(){
        createdAt = LocalDateTime.now();

        if(balance == null){
            balance = BigDecimal.ZERO;
        }

        if(status == null){
            status = AccountStatus.ACTIVE;
        }
    }
}
