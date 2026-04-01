package com.rcoem.filmrentalui.controllers;

import com.rcoem.filmrentalui.dto.ActorDTO;
import com.rcoem.filmrentalui.dto.FilmDTO;
import com.rcoem.filmrentalui.dto.FilmResponse;
import com.rcoem.filmrentalui.dto.LanguageResponse;
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
public String getFilms(@RequestParam(required = false) String keyword, 
                       @RequestParam(defaultValue = "0") int page, // Receive page here
                       Model model) {
    
    // Pass the page and a size (e.g., 20) to your service
    FilmResponse response = apiService.getAllFilms(keyword, page, 20); 
    
    model.addAttribute("films", response.getEmbedded() != null ? response.getEmbedded().getFilms() : null);
    model.addAttribute("pageData", response.getPageData()); // Pass paging info to HTML
    model.addAttribute("currentKeyword", keyword);
    return "films";
}

    // ADD FORM
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("film", new FilmDTO());
        
        // Using your existing method here
        LanguageResponse langResp = apiService.getAllLanguage(0, 100);
        model.addAttribute("languages", langResp.getEmbeddedLanguage().getLanguages());
        
        model.addAttribute("pageTitle", "Add New Film");
        return "filmform";
    }

    // EDIT FORM
    @GetMapping("/edit")
    public String showEditForm(@RequestParam Long filmId, Model model) {
        FilmDTO film = apiService.getFilmById(filmId);
        film.setFilmId(filmId.toString());
        
        // Using your existing method here
        LanguageResponse langResp = apiService.getAllLanguage(0, 100);
        model.addAttribute("languages", langResp.getEmbeddedLanguage().getLanguages());
        
        model.addAttribute("film", film);
        model.addAttribute("pageTitle", "Edit Film: " + film.getTitle());
        return "filmform";
    }

    // SAVE PROCESS
    @PostMapping("/save")
public String saveFilm(@ModelAttribute FilmDTO film, @RequestParam String languageId) {
    // languageId is now received as a String
    apiService.saveOrUpdateFilm(film, languageId);
    return "redirect:/films";
}

    // ✅ DELETE film
     @GetMapping("/delete/{id}")
    public String deleteFilm(@PathVariable Short id) {
        apiService.deleteFilm(id);
        return "redirect:/films";
    }


    @GetMapping("/actors")
public String showActors(@RequestParam Long filmId, 
                         @RequestParam String title, 
                         Model model) {
    
    // 1. Get actors list from service
    List<ActorDTO> actors = apiService.getActorsByFilm(filmId);
    
    // 2. Add to model
    model.addAttribute("actors", actors);
    model.addAttribute("filmTitle", title);
    
    // 3. Return the new actors page
    return "film-actors"; 
}
}
