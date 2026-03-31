package com.rcoem.filmrentalui.controllers;

import com.rcoem.filmrentalui.dto.FilmDTO;
import com.rcoem.filmrentalui.service.ExternalApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private ExternalApiService apiService;

    // ✅ LIST films
    @GetMapping
    public String getFilms(Model model) {
        List<FilmDTO> films = apiService.getAllFilms();
        model.addAttribute("films", films);
        return "films";
    }

    // ✅ SHOW form
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("film", new FilmDTO());
        return "film-form";
    }

    // ✅ SAVE film
    @PostMapping("/save")
    public String saveFilm(@ModelAttribute FilmDTO film) {
        apiService.saveFilm(film);
        return "redirect:/films";
    }

    // ✅ DELETE film
    @GetMapping("/delete/{id}")
    public String deleteFilm(@PathVariable Long id) {
        apiService.deleteFilm(id);
        return "redirect:/films";
    }
}
