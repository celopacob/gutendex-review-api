package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableAsync
public class GutendexApiConfiguration {

    @Bean
    public ObjectMapper objectMapperBean() {
        return new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }

    @Bean
    public RestTemplate restTemplateBean() {
        return new RestTemplate();
    }
}