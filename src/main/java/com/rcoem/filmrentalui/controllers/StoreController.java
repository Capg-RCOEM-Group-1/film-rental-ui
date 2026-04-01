package com.rcoem.filmrentalui.controllers;

import com.rcoem.filmrentalui.dto.StorePageResponse;
import com.rcoem.filmrentalui.dto.FilmResponse;
import com.rcoem.filmrentalui.service.ExternalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stores")
public class StoreController {

    private final ExternalApiService apiService;

    @Autowired
    public StoreController(ExternalApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping
    public String listStores(@RequestParam(defaultValue = "0") int page, Model model) {
        try {
            StorePageResponse response = apiService.getStores(page, 10);
            if (response != null && response.get_embedded() != null) {
                model.addAttribute("stores", response.get_embedded().getStores());
                model.addAttribute("pageData", response.getPage());
            }
        } catch (Exception e) {
            model.addAttribute("error", "Backend connection failed. Please ensure the Store Service is running.");
        }
        return "stores";
    }

    @PostMapping("/delete/{id}")
    public String deleteStore(@PathVariable Byte id) {
        try {
            apiService.deleteStore(id);
        } catch (Exception e) {
            // Log error
        }
        return "redirect:/stores";
    }

    @GetMapping("/{id}/inventory")
    public String viewStoreInventory(@PathVariable Byte id, @RequestParam(defaultValue = "0") int page, Model model) {
        try {
            FilmResponse response = apiService.getFilmsByStore(id, page, 12);
            model.addAttribute("storeId", id);
            if (response != null && response.get_embedded() != null) {
                model.addAttribute("films", response.get_embedded().getFilms());
                model.addAttribute("pageData", response.getPage());
            }
        } catch (Exception e) {
            model.addAttribute("error", "Could not load inventory.");
        }
        return "store-films";
    }
}