package com.nsu.shift.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Seller seller;

    private int amount;

    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    @CreationTimestamp
    private LocalDateTime transactionDate;

    public Transaction (Seller seller, int amount, PaymentType paymentType){
        this.seller = seller;
        this.amount = amount;
        this.paymentType = paymentType;
    }
}
