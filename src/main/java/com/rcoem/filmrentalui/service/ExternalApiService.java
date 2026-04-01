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

    //Language Backend End points
    public LanguageResponse getAllLanguage(int page, int size){
        String url = baseUrl + "/languages?page="+page+"size="+size;
        return restTemplate.getForObject(url,LanguageResponse.class);
    }

   public FilmResponse getFilms(int page, int size, int id, String name){
        String url = baseUrl+"/flims/search/findByLanguage_Id?page="+page+"&size="+size+"&id="+id+"&projection=filmView";
        return restTemplate.getForObject(url,FilmResponse.class);
   }


}
