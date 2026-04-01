package com.rcoem.filmrentalui.dto;

import java.util.List;

public class FilmResponse {

    private Embedded _embedded;
    private PageData page;

    public Embedded get_embedded() {
        return _embedded;
    }

    public PageData getPage() {
        return page;
    }

    public void set_embedded(Embedded _embedded) {
        this._embedded = _embedded;
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
