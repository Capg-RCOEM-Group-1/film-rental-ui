package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreDTO {
    private Short storeId; // Still needed for your Java logic
    private String address;
    private String city;

    // Getters and Setters
    public Short getStoreId() { return storeId; }
    public void setStoreId(Short storeId) { this.storeId = storeId; }

    @com.fasterxml.jackson.annotation.JsonProperty("_links")
    public void unpackId(java.util.Map<String, Object> links) {
        try {
            java.util.Map<String, String> self = (java.util.Map<String, String>) links.get("store");
            if (self == null) self = (java.util.Map<String, String>) links.get("self");
            if (self != null) {
                String href = self.get("href").replaceAll("\\{.*\\}", "");
                String idStr = href.substring(href.lastIndexOf('/') + 1);
                this.storeId = Short.parseShort(idStr);
            }
        } catch (Exception e) {}
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getFullDisplay() {
        return address != null ? (city != null ? address + ", " + city : address) : "";
    }
}
