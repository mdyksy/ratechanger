package com.hsbc.ratechanger.external.client;

import com.hsbc.ratechanger.commons.DateTimeProvider;
import com.hsbc.ratechanger.exchange.MonthlyExchangeRateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.valueOf;
import static java.time.YearMonth.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRatesApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ExchangeRatesApiUrlBuilder exchangeRatesApiUrlBuilder;

    @Mock
    private DateTimeProvider dateTimeProvider;

    private ExchangeRatesApiClient exchangeRatesApiClient;

    @BeforeEach
    void setUp() {
        exchangeRatesApiClient = new ExchangeRatesApiClientImpl(restTemplate, exchangeRatesApiUrlBuilder, dateTimeProvider);
    }

    @DisplayName("should call external API to get exchange rates and return those")
    @Test
    void getLastYearExchangeRates() {
        when(dateTimeProvider.dateNow()).thenReturn(LocalDate.of(2021, 7, 1));
        when(exchangeRatesApiUrlBuilder.buildGetExchangeRatesByDateUrl(any())).thenReturn("test");
        when(restTemplate.getForEntity(anyString(), eq(GetExchangeRatesApiResponse.class))).thenReturn(
                prepareRsp(of(2020, 7)), prepareRsp(of(2020, 8)),
                prepareRsp(of(2020, 9)), prepareRsp(of(2020, 10)),
                prepareRsp(of(2020, 11)), prepareRsp(of(2020, 12)),
                prepareRsp(of(2021, 1)), prepareRsp(of(2021, 2)),
                prepareRsp(of(2021, 3)), prepareRsp(of(2021, 4)),
                prepareRsp(of(2021, 5)), prepareRsp(of(2021, 6)));

        final List<MonthlyExchangeRateDto> exchangeRates = exchangeRatesApiClient.getLastYearExchangeRates();

        assertThat(exchangeRates).hasSize(12);
        verify(restTemplate, times(12)).getForEntity(anyString(), eq(GetExchangeRatesApiResponse.class));
    }

    private ResponseEntity<GetExchangeRatesApiResponse> prepareRsp(YearMonth date) {
        final GetExchangeRatesApiResponse rsp = new GetExchangeRatesApiResponse();
        rsp.setSuccess(true);
        rsp.setDate(date.atDay(1));
        rsp.setRates(Map.of("USD", valueOf(1)));

        return ResponseEntity.ok(rsp);
    }
}