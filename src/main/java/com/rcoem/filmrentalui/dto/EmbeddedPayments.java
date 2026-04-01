package com.rcoem.filmrentalui.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmbeddedPayments {
    private List<PaymentSummaryDTO> payments;
}
