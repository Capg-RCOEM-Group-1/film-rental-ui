package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerPageResponse {
    @JsonProperty("_embedded")
    private EmbeddedCustomers embedded;
    private PageData page;

    public EmbeddedCustomers getEmbedded() { return embedded; }
    public void setEmbedded(EmbeddedCustomers embedded) { this.embedded = embedded; }
    public PageData getPage() { return page; }
    public void setPage(PageData page) { this.page = page; }
}