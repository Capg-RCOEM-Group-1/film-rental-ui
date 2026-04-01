package com.rcoem.filmrentalui.dto;


import java.util.List;


public class EmbeddedFilms {
    private List<FilmDTO> films;


    public List<FilmDTO> getFilms() {
        return films;
    }


    public void setFilms(List<FilmDTO> films) {
        this.films = films;
    }
}
