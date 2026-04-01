package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreDTO {
    private Short storeId;

    @JsonProperty("addressName")
    private String address;

    @JsonProperty("cityName")
    private String city;

    @JsonProperty("_links")
    public void unpackId(Map<String, Object> links) {
        try {
            // Check for 'self' link which contains the ID in the URL
            Map<String, String> self = (Map<String, String>) links.get("self");
            if (self != null) {
                String href = self.get("href").replaceAll("\\{.*\\}", "");
                String idStr = href.substring(href.lastIndexOf('/') + 1);
                this.storeId = Short.parseShort(idStr);
            }
        } catch (Exception e) {
            // Fallback: If parsing fails, storeId remains null
        }
    }

    public String getFullDisplay() {
        if (address == null) return "";
        return (city != null) ? address + ", " + city : address;
    }


}