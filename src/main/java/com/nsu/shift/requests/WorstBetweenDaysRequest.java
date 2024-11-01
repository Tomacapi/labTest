package com.nsu.shift.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public final class WorstBetweenDaysRequest {

    private LocalDate beginDay;
    private LocalDate endDay;

    private int amount;
}
