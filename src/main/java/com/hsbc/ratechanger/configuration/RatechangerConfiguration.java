package com.hsbc.ratechanger.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hsbc.ratechanger.exception.RestTemplateErrorHandler;
import com.hsbc.ratechanger.logging.HttpTraceInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("!test")
public class RatechangerConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateErrorHandler restTemplateErrorHandler, HttpTraceInterceptor httpTraceInterceptor) {
        return new RestTemplateBuilder()
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .errorHandler(restTemplateErrorHandler)
                .interceptors(httpTraceInterceptor)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new JavaTimeModule());
    }
}
