package com.rcoem.filmrentalui.controllers;

import com.rcoem.filmrentalui.dto.CategoryDTO;
import com.rcoem.filmrentalui.dto.CategoryResponse;
import com.rcoem.filmrentalui.dto.CategoryFilmResponse;
import com.rcoem.filmrentalui.service.ExternalApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryFilmController {

    @Autowired
    private ExternalApiService apiService;

    @GetMapping
    public String categoriesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            Model model) {

        CategoryResponse response;
        try {
            if (keyword != null && !keyword.isEmpty()) {
                response = apiService.searchCategories(keyword, page, 10);
            } else {
                response = apiService.getCategories(page, 10);
            }

            if (response != null && response.getEmbedded() != null) {
                model.addAttribute("categories", response.getEmbedded().getCategories());
                model.addAttribute("page", response.getPage());
            } else {
                model.addAttribute("categories", java.util.Collections.emptyList());
                model.addAttribute("page", new com.rcoem.filmrentalui.dto.PageData());
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load categories: " + e.getMessage());
            model.addAttribute("categories", java.util.Collections.emptyList());
        }

        model.addAttribute("keyword", keyword);
        return "categories";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new CategoryDTO());
        return "categoryForm";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute CategoryDTO category, RedirectAttributes redirectAttrs) {
        try {
            if (category.getCategoryId() == null) {
                apiService.createCategory(category);
                redirectAttrs.addFlashAttribute("success", "Category created successfully!");
            } else {
                apiService.updateCategory(category.getCategoryId(), category);
                redirectAttrs.addFlashAttribute("success", "Category updated successfully!");
            }
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Failed to save category: " + e.getMessage());
        }

        return "redirect:/categories";
    }

    @GetMapping("/edit")
    public String editCategory(
            @RequestParam Byte id,
            @RequestParam String name,
            Model model) {

        CategoryDTO category = new CategoryDTO();
        category.setCategoryId(id);
        category.setName(name);

        model.addAttribute("category", category);
        return "categoryForm";
    }

    @GetMapping("/delete")
    public String deleteCategory(@RequestParam Byte id, RedirectAttributes redirectAttrs) {
        try {
            apiService.deleteCategory(id);
            redirectAttrs.addFlashAttribute("success", "Category deleted successfully!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Cannot delete category: It may have associated films or other dependencies.");
        }

        return "redirect:/categories";
    }

    @GetMapping("/films")
    public String getFilmsByCategory(
            @RequestParam Byte categoryId,
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        try {
            CategoryFilmResponse response = apiService.getFilmsByCategory(categoryId, page, 10);

            if (response != null && response.getEmbedded() != null) {
                model.addAttribute("films", response.getEmbedded().getFilms());
                model.addAttribute("page", response.getPage());
            } else {
                model.addAttribute("films", java.util.Collections.emptyList());
                model.addAttribute("page", new com.rcoem.filmrentalui.dto.PageData());
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load films: " + e.getMessage());
            model.addAttribute("films", java.util.Collections.emptyList());
        }

        model.addAttribute("categoryName", name);
        model.addAttribute("categoryId", categoryId);
        return "categoryFilms";
    }
}