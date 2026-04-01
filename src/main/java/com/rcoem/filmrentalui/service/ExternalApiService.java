package com.rcoem.filmrentalui.service;

import com.rcoem.filmrentalui.dto.CustomerPageResponse;
import com.rcoem.filmrentalui.dto.FilmDTO;
import com.rcoem.filmrentalui.dto.FilmResponse;

import java.util.List;

import com.rcoem.filmrentalui.dto.StorePageResponse;
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


    public CustomerPageResponse getCustomers(int page, int size) {
        String url = this.baseUrl + "customers?page=" + page + "&size=" + size;
        return restTemplate.getForObject(url, CustomerPageResponse.class);
    }

    public CustomerPageResponse searchCustomers(String keyword, int page, int size) {
        String url = this.baseUrl + "customers/search/search-all?keyword=" + keyword + "&page=" + page + "&size="
                + size;
        return restTemplate.getForObject(url, CustomerPageResponse.class);
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


    //-------------------------------------------- Store Services ------------------------------------------------------
    // ✅ GET ALL STORES (Paginated)
    public StorePageResponse getStores(int page, int size) {
        // REMOVED leading slash before 'stores' to prevent double slashes
        String url = String.format("%sstores?projection=storeSummary&page=%d&size=%d", baseUrl, page, size);
        return restTemplate.getForObject(url, StorePageResponse.class);
    }

    // ✅ GET FILMS BY STORE ID
    public FilmResponse getFilmsByStore(Byte storeId, int page, int size) {
        // REMOVED leading slash before 'stores'
        String url = String.format("%sstores/%d/inventories?projection=inventoryFilm&page=%d&size=%d",
                baseUrl, storeId, page, size);
        return restTemplate.getForObject(url, FilmResponse.class);
    }

    // ✅ DELETE STORE
    public void deleteStore(Byte id) {
        // REMOVED leading slash
        restTemplate.delete(baseUrl + "stores/" + id);
    }

    //------------------------------------------------------------------------------------------------------------------

}
