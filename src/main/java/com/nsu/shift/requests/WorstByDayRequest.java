package com.nsu.shift.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public final class WorstByDayRequest {

    private LocalDate day;

    private int amount;
}
