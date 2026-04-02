package com.rcoem.filmrentalui.service;

import com.rcoem.filmrentalui.dto.CustomerPageResponse;
import com.rcoem.filmrentalui.dto.FilmDTO;
import com.rcoem.filmrentalui.dto.FilmResponse;
import com.rcoem.filmrentalui.dto.PaymentDTO;
import com.rcoem.filmrentalui.dto.PaymentResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public ExternalApiService(
            @Value("${api.base-url}") String baseUrl,
            RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public CustomerPageResponse getCustomers(int page, int size) {
        String url = this.baseUrl + "customers?page=" + page + "&size=" + size;
        return restTemplate.getForObject(url, CustomerPageResponse.class);
    }

    public CustomerPageResponse searchCustomers(String keyword, int page, int size) {
        String url = this.baseUrl + "customers/search/search-all?keyword=" + keyword
                + "&page=" + page + "&size=" + size;
        return restTemplate.getForObject(url, CustomerPageResponse.class);
    }

    public List<FilmDTO> getAllFilms() {
        String url = baseUrl + "/films?projection=filmProjection";
        FilmResponse response = restTemplate.getForObject(url, FilmResponse.class);
        return response.get_embedded().getFilms();
    }

    public void saveFilm(FilmDTO film) {
        restTemplate.postForObject(baseUrl + "/films", film, FilmDTO.class);
    }

    public void deleteFilm(Long id) {
        restTemplate.delete(baseUrl + "/films/" + id);
    }

    public List<PaymentDTO> getPaymentsByStore(Byte storeId) {
        String url = baseUrl + "payments/search/findPaymentsByStaff_Store_StoreId"
                + "?storeId=" + storeId;
        PaymentResponse response = restTemplate.getForObject(url, PaymentResponse.class);
        if (response != null && response.getEmbedded() != null) {
            return response.getEmbedded().getPayments();
        }
        return new ArrayList<>();
    }

    public List<PaymentDTO> getPaymentsByStoreAndDate(Byte storeId, String date) {
        String url = baseUrl + "payments/search/findPaymentsByStaff_Store_StoreIdAndPaymentDate"
                + "?storeId=" + storeId
                + "&paymentDate=" + date + "T00:00:00";
        PaymentResponse response = restTemplate.getForObject(url, PaymentResponse.class);
        if (response != null && response.getEmbedded() != null) {
            return response.getEmbedded().getPayments();
        }
        return new ArrayList<>();
    }
}