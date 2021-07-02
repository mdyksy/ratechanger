package com.hsbc.ratechanger.external.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRatesApiUrlBuilderTest {

    @Mock
    private ExchangeRatesApiProperties exchangeRatesApiProperties;

    private ExchangeRatesApiUrlBuilder exchangeRatesApiUrlBuilder;

    @BeforeEach
    void setUp() {
        exchangeRatesApiUrlBuilder = new ExchangeRatesApiUrlBuilder(exchangeRatesApiProperties);
    }

    @DisplayName("should build URL with necessary request params and correct path")
    @Test
    void buildGetExchangeRatesByDateUrl() {
        when(exchangeRatesApiProperties.getApiKey()).thenReturn("apiKey");
        when(exchangeRatesApiProperties.getBaseUrl()).thenReturn("http://api.exchangeratesapi.io/v1/");
        when(exchangeRatesApiProperties.getCurrencies()).thenReturn(Set.of("USD,HKD,GBP"));

        final String url = exchangeRatesApiUrlBuilder.buildGetExchangeRatesByDateUrl(LocalDate.of(2021, 7, 1));

        assertThat(url)
                .startsWith("http://api.exchangeratesapi.io/v1/2021-07-01?&")
                .contains("symbols=USD,HKD,GBP", "access_key=apiKey");
    }
}