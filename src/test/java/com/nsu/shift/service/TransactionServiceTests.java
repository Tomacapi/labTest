package com.nsu.shift.service;

import com.nsu.shift.model.PaymentType;
import com.nsu.shift.model.Seller;
import com.nsu.shift.model.Transaction;
import com.nsu.shift.repository.TransactionRepository;
import com.nsu.shift.requests.CreateTransactionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest

public class TransactionServiceTests {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock SellerService sellerService;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;

    private Seller seller;

    @BeforeEach
    public void setup() {
        seller = new Seller();
        seller.setId(UUID.randomUUID());
        seller.setName("name1");
        seller.setContactInfo("123");
        seller.setRegistrationDate(LocalDate.parse("2023-02-10").atStartOfDay());

        transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setSeller(seller);
        transaction.setAmount(100);
        transaction.setPaymentType(PaymentType.CARD);
        transaction.setTransactionDate(LocalDate.parse("2024-02-10").atStartOfDay());
    }

    @Test
    public void createTransaction_success() {
        var request = new CreateTransactionRequest(transaction.getSeller().getId(), transaction.getAmount(), transaction.getPaymentType());
        given(sellerService.getSeller(eq(seller.getId()))).willReturn(seller);
        given(transactionRepository.save(any(Transaction.class))).willReturn(transaction);

        var actualTransaction = transactionService.createTransaction(request);

        assertNotNull(actualTransaction.getId());
        assertNotNull(actualTransaction.getTransactionDate());
        assertEquals(transaction.getSeller(), actualTransaction.getSeller());
        assertEquals(transaction.getAmount(), actualTransaction.getAmount());
        assertEquals(transaction.getPaymentType(), actualTransaction.getPaymentType());
    }

    @Test
    public void getTransaction() {
        given(transactionRepository.findById(eq(transaction.getId()))).willReturn(Optional.of(transaction));

        var actualTransaction = transactionService.getTransaction(transaction.getId());

        assertEquals(transaction, actualTransaction);
    }
}
