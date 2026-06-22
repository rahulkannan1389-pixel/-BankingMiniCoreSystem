package com.techpalle.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techpalle.entity.Transaction;

public interface TransactionRepository extends JpaRepository <Transaction, Serializable>{

    List<Transaction> findByAccountIdOrderByTransactionDateDesc(Long accountId);

    Optional<Transaction> findByReferenceNumber(String referenceNumber);

    long countByAccountId(Long accountId);

    List<Transaction> findByAccountIdAndTransactionTypeOrderByTransactionDateDesc(
    		Long accountId,Transaction.TransactionType transactionType);

}
