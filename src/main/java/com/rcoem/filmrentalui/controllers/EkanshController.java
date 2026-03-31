package com.rcoem.filmrentalui.controllers;

import com.rcoem.filmrentalui.dto.CustomerPageResponse;
import com.rcoem.filmrentalui.service.ExternalApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ekansh")
public class EkanshController {

    // 1. Add a logger for debugging backend failures
    private static final Logger logger = LoggerFactory.getLogger(EkanshController.class);

    private final ExternalApiService externalApiService;

    // 2. MUST add @Autowired for Spring 4.0.4 dependency injection
    @Autowired
    public EkanshController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public String customersPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        try {
            CustomerPageResponse response;

            if (keyword == null || keyword.trim().isEmpty()) {
                response = externalApiService.getCustomers(page, 20);
            } else {
                response = externalApiService.searchCustomers(keyword, page, 20);
            }

            // Safely extract the data to pass to the view
            if (response != null && response.getEmbedded() != null) {
                model.addAttribute("customers", response.getEmbedded().getCustomers());
            }
            model.addAttribute("pageData", response != null ? response.getPage() : null);
            model.addAttribute("currentKeyword", keyword);

        } catch (Exception e) {
            // 3. Log the actual technical error to the console/logs
            logger.error("Failed to fetch customer data from backend API. Page: {}, Keyword: {}", page, keyword, e);

            // If the backend is down, your UI doesn't crash. It just shows an error message.
            model.addAttribute("error", "The customer directory is currently unavailable.");
        }

        return "customers";
    }
}