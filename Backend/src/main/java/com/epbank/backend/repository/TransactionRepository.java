package com.epbank.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epbank.backend.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    List<Transaction>findByAccountId(Long accountId);
}
