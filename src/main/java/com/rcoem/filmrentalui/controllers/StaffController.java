package com.rcoem.filmrentalui.controllers;

import com.rcoem.filmrentalui.dto.PaymentSummaryResponse;
import com.rcoem.filmrentalui.dto.StaffDTO;
import com.rcoem.filmrentalui.dto.StaffPageResponse;
import com.rcoem.filmrentalui.service.ExternalApiService;
import com.rcoem.filmrentalui.service.PaymentApiService;
import com.rcoem.filmrentalui.service.StaffApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/aditya/staff")
public class StaffController {

    @Autowired
    private StaffApiService staffApiService;

    @Autowired
    private PaymentApiService paymentApiService;

    @Autowired
    private ExternalApiService externalApiService;

    /**
     * Handles the main staff list, including pagination and active/inactive filtering.
     */
    @GetMapping
    public String showStaff(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "active", required = false) Boolean active,
            Model model) {

        StaffPageResponse staffResponse;

        if (active != null) {
            System.out.println(active);
            // Filter by active status if the parameter is present
            staffResponse = staffApiService.getStaffByActiveStatus(active, page, size);
            System.out.println(staffResponse.getEmbedded().getStaffs().stream().map(StaffDTO::getActive).toList());
            model.addAttribute("currentActive", active);

        } else {
            // Default: show all staff
            staffResponse = staffApiService.getAllStaff(page, size);
        }

        model.addAttribute("staffResponse", staffResponse);
        return "staff";
    }

    /**
     * Handles the search functionality across multiple fields.
     */
    @GetMapping("/search")
    public String searchStaff(
            @RequestParam("searchTerm") String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        StaffPageResponse staffResponse = staffApiService.searchStaff(searchTerm, page, size);

        model.addAttribute("staffResponse", staffResponse);
        model.addAttribute("currentSearch", searchTerm); // Keep search term in the search box
        return "staff";
    }

    /**
     * Fetches and displays payment summaries for a specific staff member.
     */
    @GetMapping("/payments")
    public String showStaffPayments(
            @RequestParam("username") String username,
            @RequestParam("staffName") String staffName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        int size = 20;
        PaymentSummaryResponse response = paymentApiService.getStaffPaymentSummary(username, page, size);
        System.out.println(page + " " + size);
        if (response != null && response.getEmbedded() != null) {
            model.addAttribute("payments", response.getEmbedded().getPayments());
            model.addAttribute("page", response.getPage()); // Pass the HAL 'page' object
        }

        model.addAttribute("staffName", staffName);
        model.addAttribute("username", username); // Keep URL for the 'Next' link

        return "staffPaymentSummary";
    }


        @GetMapping("/add")
        public String showAddForm(Model model) {
            model.addAttribute("staff", new StaffDTO());
            model.addAttribute("stores", externalApiService.getAllStores());
            model.addAttribute("addresses", externalApiService.getAllAddresses());
            return "staff-form";
        }

        @PostMapping("/save")
        public String saveStaff(@ModelAttribute("staff") StaffDTO staff) {
            System.out.println(staff.getStaffId());
            if (staff.getStaffId() == null) {
                staffApiService.saveStaff(staff);
            } else {
                staffApiService.updateStaff(staff.getStaffId(), staff);
            }
            return "redirect:/aditya/staff";
        }

        @GetMapping("/edit/{id}")
        public String showEditForm(@PathVariable Byte id, Model model) {

            model.addAttribute("staff", staffApiService.getStaffById(id));
            model.addAttribute("stores", externalApiService.getAllStores());
            model.addAttribute("addresses", externalApiService.getAllAddresses());
            return "staff-form";
        }

    @GetMapping("/delete/{id}")
    public String deactivateStaff(@PathVariable Byte id) {
        // 1. Fetch the existing staff details
        StaffDTO staff = staffApiService.getStaffById(id);

        // 2. Flip the status to inactive
        staff.setActive(false);

        // 3. Save the update
        staffApiService.updateStaff(id, staff);

        // 4. Redirect back to the list
        return "redirect:/aditya/staff";
    }

}