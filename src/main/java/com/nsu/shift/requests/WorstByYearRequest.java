package com.nsu.shift.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class WorstByYearRequest {

    private LocalDate year;

    private int amount;
}
