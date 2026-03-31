package com.rcoem.filmrentalui.controllers;

import com.rcoem.filmrentalui.service.ExternalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FilmController {

    @Autowired
    private ExternalApiService apiService;

    @GetMapping("/")
    public String showFilms(Model model) {
        String films = apiService.fetchSomeData(); 
        model.addAttribute("films", films);
        return "films";
    }
}
