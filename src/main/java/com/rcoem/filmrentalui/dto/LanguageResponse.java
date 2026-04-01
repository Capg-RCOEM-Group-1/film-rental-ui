package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LanguageResponse {
    @JsonProperty("_embedded")
    private EmbeddedLanguage embeddedLanguage;
    @JsonProperty("page")
    private PageData pageData;

    public EmbeddedLanguage getEmbeddedLanguage() {
        return embeddedLanguage;
    }

    public void setEmbeddedLanguage(EmbeddedLanguage embeddedLanguage) {
        this.embeddedLanguage = embeddedLanguage;
    }

    public PageData getPageData() {
        return pageData;
    }

    public void setPageData(PageData pageData) {
        this.pageData = pageData;
    }
}
