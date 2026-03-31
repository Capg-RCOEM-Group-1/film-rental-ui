package com.rcoem.filmrentalui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/film")
public class MainController {
    @GetMapping
    public String mainPage(){
        return "main";
    }
}
