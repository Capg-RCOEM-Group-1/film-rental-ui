package com.rcoem.filmrentalui.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DailyPaymentDTO {
    private LocalDate date;
    private BigDecimal total;
    private long count;
}