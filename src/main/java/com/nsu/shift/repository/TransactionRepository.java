package com.nsu.shift.repository;

import com.nsu.shift.model.Seller;
import com.nsu.shift.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findAllBySeller(Seller seller);
}
