package com.hsbc.ratechanger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hsbc.ratechanger.exception.RestTemplateErrorHandler;
import com.hsbc.ratechanger.external.client.ExchangeRatesApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
@EnableConfigurationProperties({ExchangeRatesApiProperties.class})
public class SpringTestConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateErrorHandler restTemplateErrorHandler) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateErrorHandler)
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
