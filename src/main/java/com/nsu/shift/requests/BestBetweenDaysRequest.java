package com.nsu.shift.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BestBetweenDaysRequest {
    private LocalDate beginDay;
    private LocalDate endDay;
}
