package com.rcoem.filmrentalui.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Short paymentId;
    private BigDecimal amount;
    private String paymentDate; // Keep as String to handle split/formatting easily in JS
    private String customerName; 
}