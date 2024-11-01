package com.nsu.shift.service;

import com.nsu.shift.exception.ErrorCode;
import com.nsu.shift.exception.ServiceException;
import com.nsu.shift.model.Transaction;
import com.nsu.shift.repository.TransactionRepository;
import com.nsu.shift.requests.CreateTransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final SellerService sellerService;

    public Transaction createTransaction(CreateTransactionRequest createTransactionRequest) {
        if (createTransactionRequest.getAmount() <= 0) {
            throw new ServiceException(ErrorCode.INVALID_AMOUNT);
        }
        var seller = sellerService.getSeller(createTransactionRequest.getSellerId());
        var transaction = new Transaction(
                seller,
                createTransactionRequest.getAmount(),
                createTransactionRequest.getPaymentType());
        return transactionRepository.save(transaction);
    }

    public Transaction getTransaction(UUID id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND));
    }

    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getAllSellerTransaction(UUID sellerId) {
        var seller = sellerService.getSeller(sellerId);
        return transactionRepository.findAllBySeller(seller);
    }
}
