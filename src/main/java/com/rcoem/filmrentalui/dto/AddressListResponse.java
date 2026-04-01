package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressListResponse {

    @JsonProperty("_embedded")
    private EmbeddedAddresses embedded;

    // --- MANUAL GETTER AND SETTER FOR OUTER CLASS ---
    public EmbeddedAddresses getEmbedded() {
        return embedded;
    }

    public void setEmbedded(EmbeddedAddresses embedded) {
        this.embedded = embedded;
    }


}