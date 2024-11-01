package com.nsu.shift.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public final class WorstByQuarterRequest {

    private LocalDate year;

    @Min(1)
    @Max(4)
    private int quarter;

    private int amount;
}
