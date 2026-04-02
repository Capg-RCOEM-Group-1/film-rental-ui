package com.rcoem.filmrentalui.dto;

import java.util.List;

public  class EmbeddedAddresses {
    private List<AddressDTO> addresses;

    // --- MANUAL GETTER AND SETTER FOR INNER CLASS ---
    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }
}