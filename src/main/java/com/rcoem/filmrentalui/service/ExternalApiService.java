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
        String url = baseUrl + "/films/search/byCategoryId?categoryId=" + categoryId + "&page=" + page + "&size=" + size;
        System.out.println("DEBUG: Calling URL: " + url);
        CategoryFilmResponse response = restTemplate.getForObject(url, CategoryFilmResponse.class);
        System.out.println("DEBUG: Raw response: " + response);
        return response;
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
        org.springframework.http.HttpEntity<java.util.Map<String, Object>> request = new org.springframework.http.HttpEntity<>(
                payload, headers);

        restTemplate.exchange(this.baseUrl + "customers/" + customerId, org.springframework.http.HttpMethod.PUT,
                request, String.class);
    }

    public void deleteCustomer(String customerId) {
        String url = this.baseUrl + "customers/" + customerId;
        try {
            restTemplate.delete(url);
        } catch (Exception e) {
            System.err.println("Failed to delete customer " + customerId + ": " + e.getMessage());
            throw e;
        }
    }

    public String getCustomerRentalsJson(String customerId, int page, int size) {
        String url = this.baseUrl + "customer-rentals/search/by-customer?customerId=" + customerId + "&page=" + page + "&size=" + size;
        return restTemplate.getForObject(url, String.class);
    }

 // ✅ GET ALL FILMS
    public FilmResponse getAllFilms(String keyword, int page, int size) {
    String url;
    if (keyword != null && !keyword.isEmpty()) {
        url = baseUrl + "/films/search/findByTitleContaining?title=" + keyword 
            + "&projection=filmProjection&page=" + page + "&size=" + size;
    } else {
        url = baseUrl + "/films?projection=filmProjection&page=" + page + "&size=" + size;
    }
    
    try {
        return restTemplate.getForObject(url, FilmResponse.class);
    } catch (Exception e) {
        return new FilmResponse(); // Return empty if error
    }
}

    // ✅ CREATE (Send basic object to the collection endpoint)
    public void saveFilm(FilmDTO film) {
        String url = baseUrl + "/films";
        try {
            restTemplate.postForObject(url, film, String.class);
        } catch (Exception e) {
            System.err.println("Error saving film: " + e.getMessage());
        }
    }
    


    public FilmDTO getFilmById(Long id) {
    // We use the projection here so the UI sees the Language name
    String url = baseUrl + "/films/" + id + "?projection=filmProjection";
    return restTemplate.getForObject(url, FilmDTO.class);
}

public void saveOrUpdateFilm(FilmDTO film, String selectedLanguageId) {
    try {
        java.util.Map<String, Object> payload = new java.util.HashMap<>();
        payload.put("title", film.getTitle());
        payload.put("description", film.getDescription());
        payload.put("releaseYear", film.getReleaseYear());
        payload.put("rentalDuration", film.getRentalDuration());
        payload.put("rentalRate", film.getRentalRate());
        payload.put("length", film.getLength());
        payload.put("replacementCost", film.getReplacementCost());
        
        // Fix 1: Ensure Rating matches Enum Constant Name (e.g. PG_13 if applicable, or exactly the string)
        // Usually, Enum converters expect the string value. If "PG-13" is in the UI, check your backend Enum name.
        payload.put("rating", film.getRating());

        // Fix 2: Convert "Behind the Scenes" UI values to "BEHIND_THE_SCENES" for your HashSet converter
        if (film.getSpecialFeatures() != null) {
            java.util.List<String> formattedFeatures = film.getSpecialFeatures().stream()
                .map(f -> f.toUpperCase().replace(" ", "_"))
                .collect(java.util.stream.Collectors.toList());
            payload.put("specialFeatures", formattedFeatures);
        }

        // Fix 3: Ensure URI for Language is perfectly formed
        // Verify baseUrl in application.properties doesn't end with a slash if you add one here
        String languageUri = baseUrl + "/languages/" + selectedLanguageId;
        payload.put("language", languageUri);

        if (film.getFilmId() != null && !film.getFilmId().isEmpty()) {
            restTemplate.put(baseUrl + "/films/" + film.getFilmId(), payload);
        } else {
            restTemplate.postForObject(baseUrl + "/films", payload, String.class);
        }
    } catch (org.springframework.web.client.HttpClientErrorException e) {
        System.err.println("Backend Validation Error: " + e.getResponseBodyAsString());
        throw e;
    } catch (Exception e) {
        System.err.println("Save Error: " + e.getMessage());
        throw e;
    }
}

    // ✅ DELETE (Identify by Short ID)
    public void deleteFilm(Short id) {
        String url = baseUrl + "/films/" + id;
        try {
            restTemplate.delete(url);
        } catch (Exception e) {
            System.err.println("Error deleting film: " + e.getMessage());
        }
    }

   public List<ActorDTO> getActorsByFilm(Long filmId) {
    String url = baseUrl + "/films/" + filmId + "/actors?projection=actorProjection";
    
    // DEBUG: Print this URL and open it in your browser to check the data
    System.out.println("Fetching actors from: " + url);
    
    try {
        // We fetch as String first to see the RAW JSON in the console
        String rawJson = restTemplate.getForObject(url, String.class);
        System.out.println("RAW JSON FROM BACKEND: " + rawJson);

        ActorResponse response = restTemplate.getForObject(url, ActorResponse.class);
        return (response != null && response.getEmbedded() != null) ? 
                response.getEmbedded().getActors() : new java.util.ArrayList<>();
    } catch (Exception e) {
        return new java.util.ArrayList<>();
    }
}



    //Language Backend End points
    public LanguageResponse getAllLanguage(int page, int size){
        String url = baseUrl + "/languages?page="+page+"size="+size;
        return restTemplate.getForObject(url,LanguageResponse.class);
    }

   public FilmResponse getFilms(int page, int size, Byte id, String name){
        String url = baseUrl+"/films/search/findByLanguage_Id?page="+page+"&size="+size+"&id="+id+"&projection=filmView";
        FilmResponse response = restTemplate.getForObject(url,FilmResponse.class);
       System.out.println(response.getEmbedded().getFilms());
       return response;
   }


    public void createLanguage(LanguageFormDTO languageForm) {
        java.util.Map<String, Object> payload = new java.util.HashMap<>();
        payload.put("name", languageForm.getName());
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        org.springframework.http.HttpEntity<java.util.Map<String, Object>> request = new org.springframework.http.HttpEntity<>(payload, headers);

        restTemplate.postForObject(this.baseUrl + "languages", request, String.class);
    }

    public LanguageResponse searchLanguages(String name, int page, int size) {
        String url = this.baseUrl + "languages/search/findByNameContainingIgnoreCase?name=" + name + "&page=" + page + "&size="+ size;
        return restTemplate.getForObject(url, LanguageResponse.class);
    }

    public LanguageFormDTO getLanguageById(Byte id) {
        String url = this.baseUrl + "languages/" + id;
        java.util.Map<String, Object> response = restTemplate.getForObject(url, java.util.Map.class);
        if (response == null) return null;

        LanguageFormDTO check = new LanguageFormDTO();
        check.setId(id);
        check.setName((String) response.get("name"));

        return check;
    }

    public void updateLanguage(Byte id, LanguageFormDTO languageForm) {
        java.util.Map<String, Object> payload = new java.util.HashMap<>();
        payload.put("name", languageForm.getName());

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        org.springframework.http.HttpEntity<java.util.Map<String, Object>> request = new org.springframework.http.HttpEntity<>(payload, headers);

        restTemplate.exchange(this.baseUrl + "languages/" + id, org.springframework.http.HttpMethod.PUT, request, String.class);
    }

    public FilmResponse searchFilms(String name, int page, int size, Byte id, String title) {
        String url = this.baseUrl + "films/search/findByLanguage_IdAndTitleContainingIgnoreCase?id=" + id + "&page=" + page + "&size="+ size +"&title="+title;
        return restTemplate.getForObject(url, FilmResponse.class);
    }
}
