package com.rcoem.filmrentalui.dto;

import java.util.List;

public class EmbeddedCustomers {
    private List<CustomerDTO> customers;

    public List<CustomerDTO> getCustomers() { return customers; }
    public void setCustomers(List<CustomerDTO> customers) { this.customers = customers; }
}
