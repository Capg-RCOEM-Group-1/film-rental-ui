package com.rcoem.filmrentalui.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    private Short addressId;
    private String address;
    private String district;
    private String phone;

    @com.fasterxml.jackson.annotation.JsonProperty("_links")
    public void unpackId(java.util.Map<String, Object> links) {
        try {
            java.util.Map<String, String> self = (java.util.Map<String, String>) links.get("address");
            if (self == null) self = (java.util.Map<String, String>) links.get("self");
            if (self != null) {
                String href = self.get("href").replaceAll("\\{.*\\}", "");
                String idStr = href.substring(href.lastIndexOf('/') + 1);
                this.addressId = Short.parseShort(idStr);
            }
        } catch (Exception e) {}
    }

    // Formats how it looks in the HTML dropdown!
    public String getFullDisplay() {
        return address + ", " + district + " (" + phone + ")";
    }
}
