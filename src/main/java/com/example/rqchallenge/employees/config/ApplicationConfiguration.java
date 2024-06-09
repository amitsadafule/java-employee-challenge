package com.example.rqchallenge.employees.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class ApplicationConfiguration {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Bean
    public ObjectMapper objectMapper() {
        return OBJECT_MAPPER;
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder().build();
    }
}
