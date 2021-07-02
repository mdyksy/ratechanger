package com.hsbc.ratechanger.external.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties("exchange-rates-api")
@Getter
@Setter
public class ExchangeRatesApiProperties {

    private String baseUrl;
    private String apiKey;
    private Set<String> currencies;
}
