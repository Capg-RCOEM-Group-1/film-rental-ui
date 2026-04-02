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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ameya")
public class AmeyaController {

    @Autowired
    private ExternalApiService externalApiService;

    private static final Logger logger = LoggerFactory.getLogger(AmeyaController.class);


    @GetMapping("/stores")
    public String storesPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        try {
            List<StoreDTO> stores;

            // Logic to switch between a full list and a keyword search
            if (keyword != null && !keyword.trim().isEmpty()) {
                // Returns List<StoreDTO> as updated in the service
                stores = externalApiService.searchStores(keyword, page, 10);
            } else {
                // Returns List<StoreDTO> for the default view
                stores = externalApiService.getAllStores();
            }

            // Provide a non-null list to the UI to prevent Thymeleaf errors
            if (stores != null) {
                model.addAttribute("stores", stores);
            } else {
                model.addAttribute("stores", new ArrayList<StoreDTO>());
            }

            // Provide addresses list for the inline dropdown update
            model.addAttribute("addresses", externalApiService.getAllAddresses());

            // Pass the keyword back to the model so the search input stays populated
            model.addAttribute("currentKeyword", keyword);

        } catch (Exception e) {
            logger.error("Error loading stores directory: ", e);
            model.addAttribute("error", "The store directory is currently unavailable. Please try again later.");
            model.addAttribute("stores", new ArrayList<StoreDTO>());
        }

        return "stores";
    }


    @GetMapping("/stores/add")
    public String addStorePage(Model model) {
        model.addAttribute("storeForm", new StoreFormDTO());
        model.addAttribute("addresses", externalApiService.getAllAddresses());

        List<java.util.Map<String, Object>> staffList = externalApiService.getAllStaff();

        // Extract ID from the self link for each of your 2 staff members
        staffList.forEach(staff -> {
            try {
                java.util.Map<String, Object> links = (java.util.Map<String, Object>) staff.get("_links");
                java.util.Map<String, String> self = (java.util.Map<String, String>) links.get("self");
                String href = self.get("href");
                // Pulls the ID from the end of the URL (e.g., .../staff/1)
                staff.put("extractedId", href.substring(href.lastIndexOf('/') + 1));
            } catch (Exception e) {}
        });

        model.addAttribute("staffList", staffList);
        return "add-store";
    }

    @PostMapping("/stores/add")
    public String processAddStore(@ModelAttribute("storeForm") StoreFormDTO storeForm,
                                  org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            externalApiService.createStore(storeForm);
            ra.addFlashAttribute("successMessage", "Store established successfully!");
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(AmeyaController.class).error("Store creation failed", e);
            ra.addFlashAttribute("error", "Failed to create store. Ensure address is not already in use.");
        }
        return "redirect:/ameya/stores";
    }

    @PostMapping("/stores/editAddress")
    public String editStoreAddress(@RequestParam("storeId") Byte storeId,
                                   @RequestParam("addressId") Short addressId,
                                   org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            externalApiService.updateStoreAddress(storeId, addressId);
            ra.addFlashAttribute("successMessage", "Store address updated successfully!");
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(AmeyaController.class).error("Store address update failed", e);
            ra.addFlashAttribute("error", "Failed to update store address.");
        }
        return "redirect:/ameya/stores";
    }

    @GetMapping("/inventory")
    public String viewInventory(
            @RequestParam("storeId") Byte storeId,
            @RequestParam(value = "movieKeyword", required = false) String movieKeyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {
        try {
            List<InventoryResponse.InventoryItem> rawList = externalApiService.getInventoriesByStore(storeId);

            // 1. Aggregate
            java.util.Map<String, FilmCountDTO> aggregateMap = new java.util.HashMap<>();
            if (rawList != null) {
                for (InventoryResponse.InventoryItem item : rawList) {
                    if (item.getFilm() != null) {
                        String title = (String) item.getFilm().get("title");
                        if (title != null) {
                            if (aggregateMap.containsKey(title)) {
                                aggregateMap.get(title).setCount(aggregateMap.get(title).getCount() + 1);
                            } else {
                                Integer length = (Integer) item.getFilm().getOrDefault("length", 0);
                                aggregateMap.put(title, new FilmCountDTO(title, 1L, null, length)); // Rating set to null
                            }
                        }
                    }
                }
            }

            // 2. Filter by Movie Name
            List<FilmCountDTO> allMovies = new java.util.ArrayList<>(aggregateMap.values());
            if (movieKeyword != null && !movieKeyword.trim().isEmpty()) {
                String search = movieKeyword.toLowerCase();
                allMovies = allMovies.stream()
                        .filter(f -> f.getTitle().toLowerCase().contains(search))
                        .collect(java.util.stream.Collectors.toList());
            }
            allMovies.sort(java.util.Comparator.comparing(FilmCountDTO::getTitle));

            // 3. Pagination (50 per page)
            int pageSize = 50;
            int totalItems = allMovies.size();
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);

            int start = Math.min(page * pageSize, totalItems);
            int end = Math.min(start + pageSize, totalItems);
            List<FilmCountDTO> pagedMovies = allMovies.subList(start, end);

            model.addAttribute("inventory", pagedMovies);
            model.addAttribute("storeId", storeId);
            model.addAttribute("movieKeyword", movieKeyword);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalResults", totalItems);

        } catch (Exception e) {
            model.addAttribute("error", "Failed to load inventory.");
        }
        return "inventory";
    }




}