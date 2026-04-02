package com.rcoem.filmrentalui.service;

import com.rcoem.filmrentalui.dto.PaymentSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentApiService {

    private final String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public PaymentApiService(@Value("${api.base-url}") String baseUrl) {
        this.baseUrl = baseUrl + "payments";
    }

    public PaymentSummaryResponse getStaffPaymentSummary(String username, int page, int size) {
        try {
            String url = baseUrl + "/search/findByStaff_Username?username="
                    + username +
                    "&page=" + page +
                    "&size=" + size;

            return restTemplate.getForObject(url, PaymentSummaryResponse.class);
        } catch (Exception e) {
            System.err.println("Error fetching payments: " + e.getMessage());
            return new PaymentSummaryResponse();
        }
    }
}
