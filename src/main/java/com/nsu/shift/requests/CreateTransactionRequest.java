package com.nsu.shift.requests;

import com.nsu.shift.model.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateTransactionRequest {

    private UUID sellerId;

    private int amount;

    private PaymentType paymentType;
}
