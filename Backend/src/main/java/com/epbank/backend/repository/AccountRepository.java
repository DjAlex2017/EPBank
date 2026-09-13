package com.epbank.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epbank.backend.model.Account;

/*
JpaRepository<Account, Long>
- Account is the entity this repository manages.
- Long is the data type of the Account primary key (id).

Built-in JpaRepository methods.

save(account)
- Saves a new Account to the database or updates an existing Account.

findById(id)
- Finds an Account using its primary key (id).
- Returns an Optional<Account>.

findAll()
- Returns all Account records from the database.

delete(account)
- Deletes the given Account from the database.

deleteById(id)
- Deletes an Account using its primary key (id).

existsById(id)
- Checks whether an Account with the given id exists.

count()
- Returns the total number of Account records in the database.
 */

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);
    
    List<Account> findByCustomerId(Long customerId);
}
