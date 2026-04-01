package com.rcoem.filmrentalui.controllers;

import com.rcoem.filmrentalui.dto.CustomerFormDTO;
import com.rcoem.filmrentalui.dto.CustomerPageResponse;
import com.rcoem.filmrentalui.service.ExternalApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/customers")
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

    @GetMapping("/customers/new")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customerForm", new CustomerFormDTO());
        model.addAttribute("stores", externalApiService.getAllStores());
        model.addAttribute("addresses", externalApiService.getAllAddresses());
        return "add-customer"; // Points to add-customer.html
    }

    // 2. Process the Submission
    @PostMapping("/customers")
    public String processAddCustomer(@ModelAttribute("customerForm") CustomerFormDTO customerForm,
                                     RedirectAttributes redirectAttributes) {
        try {
            externalApiService.createCustomer(customerForm);
            redirectAttributes.addFlashAttribute("successMessage", "Customer successfully created!");
            return "redirect:/ekansh/customers";
        } catch (Exception e) {
            logger.error("Failed to create customer", e);
            redirectAttributes.addFlashAttribute("error", "Failed to create customer. Ensure backend is running.");
            return "redirect:/ekansh/customers";
        }
    }

    @GetMapping("/customers/edit")
    public String showEditCustomerForm(@RequestParam("customerId") String customerId, Model model) {
        CustomerFormDTO customerForm = externalApiService.getCustomerByIdForEdit(customerId);
        if (customerForm == null) {
            return "redirect:/ekansh/customers";
        }
        model.addAttribute("customerForm", customerForm);
        model.addAttribute("stores", externalApiService.getAllStores());
        model.addAttribute("addresses", externalApiService.getAllAddresses());
        return "update-customer";
    }

    @PostMapping("/customers/edit")
    public String processEditCustomer(@ModelAttribute("customerForm") CustomerFormDTO customerForm,
                                      RedirectAttributes redirectAttributes) {
        try {
            externalApiService.updateCustomer(customerForm.getCustomerId(), customerForm);
            redirectAttributes.addFlashAttribute("successMessage", "Customer successfully updated!");
            return "redirect:/ekansh/customers";
        } catch (Exception e) {
            logger.error("Failed to update customer", e);
            redirectAttributes.addFlashAttribute("error", "Failed to update customer. Ensure backend is running.");
            return "redirect:/ekansh/customers";
        }
    }
}