package com.rcoem.filmrentalui.controllers;

import com.rcoem.filmrentalui.dto.DailyPaymentDTO;
import com.rcoem.filmrentalui.dto.PaymentDTO;
import com.rcoem.filmrentalui.service.ExternalApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final ExternalApiService externalApiService;

    @Autowired
    public PaymentController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    /**
     * Serves the main HTML page for a specific store's payments.
     * Accessible via: /payment/details?storeId=1&name=MyStore
     */
    @GetMapping("/details")
    public String paymentsPage(
            @RequestParam("storeId") Byte storeId,
            @RequestParam(value = "name", required = false) String storeName,
            Model model) {
        
        logger.info("Loading payment details page for Store ID: {}", storeId);
        
        model.addAttribute("storeId", storeId);
        model.addAttribute("storeName", (storeName == null || storeName.isEmpty()) ? "Store " + storeId : storeName);
        
        return "payment"; // References payment.html in src/main/resources/templates
    }

    /**
     * API Endpoint for AJAX: Get all payments for a store.
     */
    @GetMapping("/api/byStore")
    @ResponseBody
    public List<PaymentDTO> getPaymentsByStore(@RequestParam("storeId") Byte storeId) {
        logger.debug("Fetching all payments for store: {}", storeId);
        return externalApiService.getPaymentsByStore(storeId);
    }

    /**
     * API Endpoint for AJAX: Get payments for a store on a specific date.
     * Input 'paymentDate' can be ISO (2023-10-01T00:00:00) or simple (2023-10-01).
     */
    @GetMapping("/api/byStoreAndDate")
    @ResponseBody
    public List<PaymentDTO> getPaymentsByStoreAndDate(
            @RequestParam("storeId") Byte storeId,
            @RequestParam("paymentDate") String paymentDate) {
        
        // Clean the date string to ensure it matches backend LocalDate format (YYYY-MM-DD)
        String formattedDate = paymentDate.contains("T") 
                ? paymentDate.split("T")[0] 
                : paymentDate;

        logger.debug("Fetching payments for store: {} on date: {}", storeId, formattedDate);
        return externalApiService.getPaymentsByStoreAndDate(storeId, formattedDate);
    }

    /**
     * API Endpoint for AJAX: Get daily aggregated totals.
     */
    @GetMapping("/api/dailyTotals")
    @ResponseBody
    public List<DailyPaymentDTO> getDailyTotals(@RequestParam("storeId") Byte storeId) {
        logger.debug("Fetching daily totals for store: {}", storeId);
        return externalApiService.getDailyPaymentTotals(storeId);
    }
}

// package com.rcoem.filmrentalui.controllers;

// import com.rcoem.filmrentalui.dto.DailyPaymentDTO;
// import com.rcoem.filmrentalui.dto.PaymentDTO;
// import com.rcoem.filmrentalui.service.ExternalApiService;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.ResponseBody;

// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.List;

// @Controller
// @RequestMapping("/payment")
// public class PaymentController {

//     private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

//     private final ExternalApiService externalApiService;

//     @Autowired
//     public PaymentController(ExternalApiService externalApiService) {
//         this.externalApiService = externalApiService;
//     }

//     @GetMapping("/details")
//     public String paymentsPage(
//             @RequestParam(value = "storeId") Byte storeId,
//             @RequestParam(value = "name", required = false, defaultValue = "") String storeName,
//             @RequestParam(value = "date", required = false) String date,
//             Model model) {

//         try {
//             List<PaymentDTO> payments;

//             if (date != null && !date.isEmpty()) {
//                 payments = externalApiService.getPaymentsByStoreAndDate(storeId, date);
//             } else {
//                 payments = externalApiService.getPaymentsByStore(storeId);
//             }

//             BigDecimal total = payments.stream()
//                     .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
//                     .reduce(BigDecimal.ZERO, BigDecimal::add);

//             model.addAttribute("payments", payments);
//             model.addAttribute("total", total);
//             model.addAttribute("storeId", storeId);
//             model.addAttribute("storeName",
//                     storeName.isEmpty() ? "Store " + storeId : storeName);
//             model.addAttribute("selectedDate", date);

//         } catch (Exception e) {
//             logger.error("Failed to fetch payments for store {}", storeId, e);
//             model.addAttribute("error", "Payment data is currently unavailable.");
//             model.addAttribute("payments", new ArrayList<>());
//         }

//         return "payment";
//     }

//     @GetMapping("/api/byStore")
//     @ResponseBody
//     public List<PaymentDTO> getPaymentsByStore(
//             @RequestParam("storeId") Byte storeId) {
//         return externalApiService.getPaymentsByStore(storeId);
//     }

//     @GetMapping("/api/byStoreAndDate")
//     @ResponseBody
//     public List<PaymentDTO> getPaymentsByStoreAndDate(
//             @RequestParam("storeId") Byte storeId,
//             @RequestParam("paymentDate") String paymentDate) {
//         return externalApiService.getPaymentsByStoreAndDate(storeId, paymentDate);
//     }

//     // ← only new addition
//     @GetMapping("/api/dailyTotals")
//     @ResponseBody
//     public List<DailyPaymentDTO> getDailyTotals(
//             @RequestParam("storeId") Byte storeId) {
//         return externalApiService.getDailyPaymentTotals(storeId);
//     }
// }