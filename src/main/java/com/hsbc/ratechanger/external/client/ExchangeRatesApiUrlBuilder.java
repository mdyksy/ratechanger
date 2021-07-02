package com.hsbc.ratechanger.external.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

import static java.lang.String.format;
import static java.lang.String.join;

@Component
@RequiredArgsConstructor
class ExchangeRatesApiUrlBuilder {

    private static final String ACCESS_KEY_REQ_PARAM = "access_key";
    private static final String SYMBOLS_REQ_PARAM = "symbols";

    private final ExchangeRatesApiProperties exchangeRatesApiProperties;

    String buildGetExchangeRatesByDateUrl(LocalDate date) {
        return exchangeRatesApiProperties.getBaseUrl() +
                date.toString() +
                requestParams(Map.of(
                        ACCESS_KEY_REQ_PARAM, exchangeRatesApiProperties.getApiKey(),
                        SYMBOLS_REQ_PARAM, join(",", exchangeRatesApiProperties.getCurrencies()))
                );
    }

    private String requestParams(Map<String, String> params) {
        return params.entrySet()
                .stream()
                .map(entry -> format("%s=%s", entry.getKey(), entry.getValue()))
                .reduce("?", (s, s2) -> format("%s&%s", s, s2), String::concat);
    }
}
