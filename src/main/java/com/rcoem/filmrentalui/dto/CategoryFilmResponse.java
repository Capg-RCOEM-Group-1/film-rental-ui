package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CategoryFilmResponse {
    @JsonProperty("_embedded")
    private EmbeddedFilms embedded;
    private PageData page;
}