package com.rcoem.filmrentalui.controllers;

import com.rcoem.filmrentalui.dto.CustomerPageResponse;
import com.rcoem.filmrentalui.dto.FilmResponse;
import com.rcoem.filmrentalui.dto.LanguageResponse;
import com.rcoem.filmrentalui.service.ExternalApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequestMapping("/mandar")
public class LanguageController {
    private static final Logger logger = LoggerFactory.getLogger(LanguageController.class);
    private final ExternalApiService externalApiService;

    // 2. MUST add @Autowired for Spring 4.0.4 dependency injection
    @Autowired
    public LanguageController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @RequestMapping(value = "/languages", method = RequestMethod.GET)
    public String LanguagesPage(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {

        try {
            LanguageResponse response;
            response = externalApiService.getAllLanguage(page, 20);

            // Safely extract the data to pass to the view
            if (response != null && response.getEmbeddedLanguage() != null) {
                model.addAttribute("languages", response.getEmbeddedLanguage().getLanguages());
            }
            model.addAttribute("pageData", response != null ? response.getPageData() : null);
        } catch (Exception e) {
            // 3. Log the actual technical error to the console/logs
            logger.error("Failed to fetch customer data from backend API. Page: {}", page, e);

            // If the backend is down, your UI doesn't crash. It just shows an error message.
            model.addAttribute("error", "The language directory is currently unavailable.");
        }

        return "languages";

    }
    @RequestMapping("/language/films")
    public String getFilmsLanguage(@RequestParam(value = "page", defaultValue = "0") int page, Model model,@RequestParam Byte id,@RequestParam String name){
        try{
            FilmResponse response;
            response = externalApiService.getFilms(page,20,id,name);
            if(response != null && response.getEmbedded() != null){
                model.addAttribute("films",response.getEmbedded().getFilms());
            }
            model.addAttribute("language",name);
            model.addAttribute("pageData", response != null ? response.getPageData() : null);
        }catch (Exception e){
            logger.error("Failed to fetch film data from backend API. Page: {}",page,e);
            model.addAttribute("error", "The language directory is currently unavailable.");
        }
        return "films";
    }
}