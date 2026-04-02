package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentSummaryResponse {

    @JsonProperty("_embedded")
    private EmbeddedPayments embedded;
    private PageData page;
}
