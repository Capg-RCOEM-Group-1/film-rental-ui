package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rcoem.filmrentalui.dto.PageData;

public class CategoryResponse {

    @JsonProperty("_embedded")
    private EmbeddedCategories embedded;

    private PageData page;

    public EmbeddedCategories getEmbedded() {
        return embedded;
    }

    public void setEmbedded(EmbeddedCategories embedded) {
        this.embedded = embedded;
    }

    public PageData getPage() {
        return page;
    }

    public void setPage(PageData page) {
        this.page = page;
    }
}