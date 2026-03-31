package com.rcoem.filmrentalui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Direct instantiation is the standard way in Spring 4.x
        return new RestTemplate();
    }
}
