package com.nsu.shift.controller;

import com.nsu.shift.model.Transaction;
import com.nsu.shift.requests.CreateTransactionRequest;
import com.nsu.shift.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public Transaction createTransaction(@RequestBody CreateTransactionRequest createTransactionRequest) {
        return transactionService.createTransaction(createTransactionRequest);
    }

    @GetMapping("/{id}")
    public Transaction getTransaction(@PathVariable UUID id) {
        return transactionService.getTransaction(id);
    }

    @GetMapping
    public List<Transaction> getAllTransaction() {
        return transactionService.getAllTransaction();
    }

    @GetMapping("/{sellerId}")
    public List<Transaction> getAllSellerTransaction(@PathVariable UUID sellerId) {
        return transactionService.getAllSellerTransaction(sellerId);
    }
}
