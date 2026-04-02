package com.rcoem.filmrentalui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyPaymentSummary {
    private LocalDate date;
    private BigDecimal total;
    private long count;
}