package com.rcoem.filmrentalui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FilmResponse {
    @JsonProperty("_embedded")
    private Embedded embedded;

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded _embedded) {
        this.embedded = _embedded;
    }

    public static class Embedded {
        private List<FilmDTO> films;

        public List<FilmDTO> getFilms() {
            return films;
        }

        public void setFilms(List<FilmDTO> films) {
            this.films = films;
        }
    }
    @JsonProperty("page")
    private PageData page;

    public PageData getPageData() {
        return page;
    }

    public void setPageData(PageData pageData) {
        this.page = pageData;
    }
}


// package com.rcoem.filmrentalui.dto;

// public class FilmResponse {

//     private EmbeddedFilms _embedded;

//     public EmbeddedFilms get_embedded() {
//         return _embedded;
//     }

//     public void set_embedded(EmbeddedFilms _embedded) {
//         this._embedded = _embedded;
//     }
// }
