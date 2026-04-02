package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryResponse {
    @JsonProperty("_embedded")
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Embedded {
        private List<InventoryItem> inventories;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InventoryItem {
        // We use Map here to be 100% sure we get the data regardless of DTO issues
        private Map<String, Object> film;
    }
}