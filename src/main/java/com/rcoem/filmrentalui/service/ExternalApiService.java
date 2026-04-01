package com.rcoem.filmrentalui.service;

import com.rcoem.filmrentalui.dto.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    // Constructor injection (Best Practice)
    @Autowired
    public ExternalApiService(
            @Value("${api.base-url}") String baseUrl,
            RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    // Write your Backend API CALLS HERE

    // CATEGORY Calls


    public CategoryFilmResponse getFilmsByCategory(Byte categoryId, int page, int size) {
        // Use category ID to fetch films through film_category join table
        String url = baseUrl + "/films/search/byCategoryId?categoryId=" + categoryId + "&page=" + page + "&size=" + size;
        return restTemplate.getForObject(url, CategoryFilmResponse.class);
    }

    public CategoryResponse getCategories(int page, int size) {
        String url = baseUrl + "/categories?page=" + page + "&size=" + size;
        return restTemplate.getForObject(url, CategoryResponse.class);
    }

    public CategoryResponse searchCategories(String keyword, int page, int size) {
        String url = baseUrl + "/categories/search/byName?name=" + keyword + "&page=" + page + "&size=" + size;
        return restTemplate.getForObject(url, CategoryResponse.class);
    }

    public void createCategory(CategoryDTO category) {
        String url = baseUrl + "/categories";
        restTemplate.postForObject(url, category, CategoryDTO.class);
    }

    public void updateCategory(Byte id, CategoryDTO category) {
        String url = baseUrl + "/categories/" + id;
        restTemplate.put(url, category);
    }

    public void deleteCategory(Byte id) {
        String url = baseUrl + "/categories/" + id;
        restTemplate.delete(url);
    }




    // CUSTOMER Calls

    public CustomerPageResponse getCustomers(int page, int size) {
        String url = this.baseUrl + "customers?page=" + page + "&size=" + size;
        return restTemplate.getForObject(url, CustomerPageResponse.class);
    }

    public CustomerPageResponse searchCustomers(String keyword, int page, int size) {
        String url = this.baseUrl + "customers/search/search-all?keyword=" + keyword + "&page=" + page + "&size="
                + size;
        return restTemplate.getForObject(url, CustomerPageResponse.class);
    }


    public List<CustomerStoreDTO> getAllStores() {
        // Add the projection parameter to the URL!
        String url = this.baseUrl + "stores?projection=storeInfo";

        try {
            CustomerStoreListResponse response = restTemplate.getForObject(url, CustomerStoreListResponse.class);
            return response != null && response.getEmbedded() != null ? response.getEmbedded().getStores() : null;
        } catch (Exception e) {
            System.err.println("Failed to fetch stores: " + e.getMessage());
            return null;
        }
    }

    public List<CustomerAddressDTO> getAllAddresses() {
        // Use a placeholder {size} in the URL
        String url = this.baseUrl + "addresses?size={size}";

        try {
            // Pass the integer 1000 as the final argument to safely replace the {size} placeholder
            CustomerAddressListResponse response = restTemplate.getForObject(url, CustomerAddressListResponse.class, 1000);

            return response != null && response.getEmbedded() != null ? response.getEmbedded().getAddresses() : null;
        } catch (Exception e) {
            // Add a log here so if it fails again, you know exactly why without crashing the whole app
            System.err.println("Failed to fetch addresses for dropdown: " + e.getMessage());
            return null;
        }
    }

    public void createCustomer(CustomerFormDTO formDTO) {
        // Build the specific JSON payload Spring Data REST expects
        java.util.Map<String, Object> payload = new java.util.HashMap<>();
        payload.put("firstName", formDTO.getFirstName());
        payload.put("lastName", formDTO.getLastName());
        payload.put("email", formDTO.getEmail());
        payload.put("active", 1); // Use 1 for true just in case, or true. Using 1 is safe for tinyint schemas.

        // Crucial: Spring Data REST requires exact URIs for relationships, not just IDs!
        payload.put("store", this.baseUrl + "stores/" + formDTO.getStoreId());
        payload.put("address", this.baseUrl + "addresses/" + formDTO.getAddressId());

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        org.springframework.http.HttpEntity<java.util.Map<String, Object>> request = new org.springframework.http.HttpEntity<>(payload, headers);

        restTemplate.postForObject(this.baseUrl + "customers", request, String.class);
    }

    public CustomerFormDTO getCustomerByIdForEdit(String customerId) {
        String url = this.baseUrl + "customers/" + customerId;
        java.util.Map<String, Object> response = restTemplate.getForObject(url, java.util.Map.class);
        if (response == null) return null;

        CustomerFormDTO check = new CustomerFormDTO();
        check.setCustomerId(customerId);
        check.setFirstName((String) response.get("firstName"));
        check.setLastName((String) response.get("lastName"));
        check.setEmail((String) response.get("email"));

        java.util.Map<String, Object> links = (java.util.Map<String, Object>) response.get("_links");
        if (links != null) {
            java.util.Map<String, String> storeLink = (java.util.Map<String, String>) links.get("store");
            if (storeLink != null) {
                String href = storeLink.get("href").replaceAll("\\{.*\\}", "");
                String idStr = href.substring(href.lastIndexOf('/') + 1);
                try { check.setStoreId(Byte.parseByte(idStr)); } catch(Exception e){}
            }
            java.util.Map<String, String> addressLink = (java.util.Map<String, String>) links.get("address");
            if (addressLink != null) {
                String href = addressLink.get("href").replaceAll("\\{.*\\}", "");
                String idStr = href.substring(href.lastIndexOf('/') + 1);
                try { check.setAddressId(Short.parseShort(idStr)); } catch(Exception e){}
            }
        }
        return check;
    }

    public void updateCustomer(String customerId, CustomerFormDTO formDTO) {
        java.util.Map<String, Object> payload = new java.util.HashMap<>();
        payload.put("firstName", formDTO.getFirstName());
        payload.put("lastName", formDTO.getLastName());
        payload.put("email", formDTO.getEmail());
        payload.put("active", 1);

        payload.put("store", this.baseUrl + "stores/" + formDTO.getStoreId());
        payload.put("address", this.baseUrl + "addresses/" + formDTO.getAddressId());

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        org.springframework.http.HttpEntity<java.util.Map<String, Object>> request = new org.springframework.http.HttpEntity<>(payload, headers);

        restTemplate.exchange(this.baseUrl + "customers/" + customerId, org.springframework.http.HttpMethod.PUT, request, String.class);
    }

 // ✅ GET ALL FILMS
    public List<FilmDTO> getAllFilms() {
        String url = baseUrl + "/films?projection=filmProjection";

        FilmResponse response =
                restTemplate.getForObject(url, FilmResponse.class);

        return response.get_embedded().getFilms();
    }

    // ✅ CREATE FILM
    public void saveFilm(FilmDTO film) {
        // film.setActors(null); 

        restTemplate.postForObject(baseUrl + "/films", film, FilmDTO.class);
    }

    // ✅ DELETE FILM
    public void deleteFilm(Long id) {
        restTemplate.delete(baseUrl + "/films/" + id);
    }



}
