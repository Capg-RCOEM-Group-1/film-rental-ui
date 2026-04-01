package com.rcoem.filmrentalui.dto;

import java.util.List;

public class EmbeddedCategories {

    private List<CategoryDTO> categories;

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }
}