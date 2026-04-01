package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerStoreListResponse {

    @JsonProperty("_embedded")
    private EmbeddedData embedded;

    public EmbeddedData getEmbedded() {
        return embedded;
    }

    public void setEmbedded(EmbeddedData embedded) {
        this.embedded = embedded;
    }

    public List<CustomerStoreDTO> getStores() {
        return embedded != null ? embedded.getStores() : List.of();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EmbeddedData {

        // This perfectly matches the "stores" array in your JSON
        private List<CustomerStoreDTO> stores;

        public List<CustomerStoreDTO> getStores() {
            return stores;
        }

        public void setStores(List<CustomerStoreDTO> stores) {
            this.stores = stores;
        }
    }
}