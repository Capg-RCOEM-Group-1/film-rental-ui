package com.rcoem.filmrentalui.controllers;

import com.rcoem.filmrentalui.service.ExternalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/film")
public class MainController {

    private final ExternalApiService externalApiService;

    @Autowired
    public MainController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }


    @GetMapping
    public String mainPage(){
        return "index";
    }
}
