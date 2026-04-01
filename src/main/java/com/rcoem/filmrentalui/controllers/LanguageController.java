package com.rcoem.filmrentalui.controllers;

import com.rcoem.filmrentalui.dto.*;
import com.rcoem.filmrentalui.service.ExternalApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String LanguagesPage(@RequestParam(value = "page", defaultValue = "0") int page,@RequestParam(value = "name", required = false) String name, Model model) {

        try {
            LanguageResponse response;
            if (name == null || name.trim().isEmpty()) {
                response = externalApiService.getAllLanguage(page, 20);
            } else {
                response = externalApiService.searchLanguages(name, page, 20);
            }

            // Safely extract the data to pass to the view
            if (response != null && response.getEmbeddedLanguage() != null) {
                model.addAttribute("languages", response.getEmbeddedLanguage().getLanguages());
            }
            model.addAttribute("pageData", response != null ? response.getPageData() : null);
            model.addAttribute("name", name);
        } catch (Exception e) {
            // 3. Log the actual technical error to the console/logs
            logger.error("Failed to fetch customer data from backend API. Page: {}", page, e);

            // If the backend is down, your UI doesn't crash. It just shows an error message.
            model.addAttribute("error", "The language directory is currently unavailable.");
        }

        return "languages";

    }
    @RequestMapping(value = "/language/films",method = RequestMethod.GET)
    public String getFilmsLanguage(@RequestParam(value = "page", defaultValue = "0") int page, Model model,@RequestParam Byte id,@RequestParam String name,@RequestParam(value = "title", required = false) String title){
        try{
            FilmResponse response;
            if (title == null || title.trim().isEmpty()) {
                response = externalApiService.getFilms(page,20,id,name);
            } else {
                response = externalApiService.searchFilms(name, page, 20,id,title);
            }
            if(response != null && response.getEmbedded() != null){
                model.addAttribute("films",response.getEmbedded().getFilms());
            }
            model.addAttribute("id",id);
            model.addAttribute("name",name);
            model.addAttribute("title",title);
            model.addAttribute("pageData", response != null ? response.getPageData() : null);
        }catch (Exception e){
            logger.error("Failed to fetch film data from backend API. Page: {}",page,e);
            model.addAttribute("error", "The language directory is currently unavailable.");
        }
        return "films-language";
    }
    @GetMapping("languages/new")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("languageForm", new LanguageFormDTO());
        return "add-language";
    }
    @PostMapping("/languages")
    public String processAddCustomer(@ModelAttribute("customerForm") LanguageFormDTO languageForm, RedirectAttributes redirectAttributes) {
        try {
            externalApiService.createLanguage(languageForm);
            redirectAttributes.addFlashAttribute("successMessage", "Customer successfully created!");
            return "redirect:/mandar/languages";
        } catch (Exception e) {
            logger.error("Failed to create customer", e);
            redirectAttributes.addFlashAttribute("error", "Failed to create customer. Ensure backend is running.");
            return "redirect:/mandar/languages";
        }
    }
    @GetMapping("/languages/edit")
    public String showEditCustomerForm(@RequestParam("id") Byte id, Model model) {
        LanguageFormDTO languageForm = externalApiService.getLanguageById(id);
        if (languageForm == null) {
            return "redirect:/mandar/languages";
        }
        model.addAttribute("languageForm", languageForm);
        return "update-language.html";
    }
    @PostMapping("/languages/edit")
    public String processEditCustomer(@ModelAttribute("languageForm") LanguageFormDTO languageForm, RedirectAttributes redirectAttributes) {
        try {
            externalApiService.updateLanguage(languageForm.getId(), languageForm);
            redirectAttributes.addFlashAttribute("successMessage", "Customer successfully updated!");
            return "redirect:/mandar/languages";
        } catch (Exception e) {
            logger.error("Failed to update customer", e);
            redirectAttributes.addFlashAttribute("error", "Failed to update customer. Ensure backend is running.");
            return "redirect:/mandar/languages";
        }
    }
}