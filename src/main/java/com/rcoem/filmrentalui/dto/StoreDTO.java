package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.sql.Timestamp;
import java.util.Map;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class StoreDTO {
    private Byte storeId;
    private String address;
    private String city;
    private Timestamp lastUpdate;

    // JACKSON uses this to map the "_links" part of your JSON to your storeId
    @JsonProperty("_links")
    public void setLinks(Map<String, Object> links) {
        try {
            Map<String, String> self = (Map<String, String>) links.get("self");
            String href = self.get("href");
            // This extracts '1' from 'http://localhost:8080/stores/1'
            this.storeId = Byte.parseByte(href.substring(href.lastIndexOf("/") + 1));
        } catch (Exception e) {
            this.storeId = 0;
        }
    }
}