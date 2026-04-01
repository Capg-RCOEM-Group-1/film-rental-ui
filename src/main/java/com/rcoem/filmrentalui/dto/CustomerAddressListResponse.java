package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CustomerAddressListResponse {

    @JsonProperty("_embedded")
    private EmbeddedAddresses embedded;

    // --- MANUAL GETTER AND SETTER FOR OUTER CLASS ---
    public EmbeddedAddresses getEmbedded() {
        return embedded;
    }

    public void setEmbedded(EmbeddedAddresses embedded) {
        this.embedded = embedded;
    }

    // --- INNER CLASS ---
    public static class EmbeddedAddresses {
        private List<CustomerAddressDTO> addresses;

        // --- MANUAL GETTER AND SETTER FOR INNER CLASS ---
        public List<CustomerAddressDTO> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<CustomerAddressDTO> addresses) {
            this.addresses = addresses;
        }
    }
}