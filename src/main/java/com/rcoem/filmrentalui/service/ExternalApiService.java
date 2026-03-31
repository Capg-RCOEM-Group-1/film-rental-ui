package com.rcoem.filmrentalui.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    // Constructor injection (Best Practice)
    public ExternalApiService(
            @Value("${api.base-url}") String baseUrl,
            RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public String fetchSomeData() {
        // Append your specific endpoint to the dynamic base URL
        String targetUrl = this.baseUrl + "/users";

        // Make the actual call
        return restTemplate.getForObject(targetUrl, String.class);
    }
}
