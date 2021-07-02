package com.hsbc.ratechanger.external;

import com.hsbc.ratechanger.exchange.MonthlyExchangeRateDto;
import com.hsbc.ratechanger.external.client.ExchangeRatesApiClient;
import com.hsbc.ratechanger.persistence.MonthlyExchangeRateEntity;
import com.hsbc.ratechanger.persistence.MonthlyExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.valueOf;
import static java.time.LocalDate.of;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetchExchangeRatesServiceTest {

    @Mock
    private ExchangeRatesApiClient exchangeRatesApiClient;

    @Mock
    private MonthlyExchangeRateRepository monthlyExchangeRateRepository;

    private FetchExchangeRatesService fetchExchangeRatesService;

    @BeforeEach
    void setUp() {
        fetchExchangeRatesService = new FetchExchangeRatesServiceImpl(exchangeRatesApiClient, monthlyExchangeRateRepository);
    }

    @DisplayName("when there aren't any new exchange rates then should do nothing")
    @Test
    void nonNewExchangeRates() {
        final List<MonthlyExchangeRateDto> exchangeRates = new ArrayList<>();
        exchangeRates.add(new MonthlyExchangeRateDto(of(2021, 6, 1), Map.of("USD", valueOf(1))));
        when(exchangeRatesApiClient.getLastYearExchangeRates()).thenReturn(exchangeRates);
        final MonthlyExchangeRateEntity entity = new MonthlyExchangeRateEntity();
        entity.setDate(of(2021, 6, 1));
        entity.setExchangeRates(Map.of("USD", valueOf(1)));
        when(monthlyExchangeRateRepository.findByDateIn(List.of(of(2021, 6, 1))))
                .thenReturn(List.of(entity));

        fetchExchangeRatesService.fetchRateExchangesFromLastYear();

        verify(monthlyExchangeRateRepository, never()).save(any());
    }

    @DisplayName("when there are new exchange rates then should add them into DB")
    @Test
    void allNewExchangeRates() {
        final List<MonthlyExchangeRateDto> exchangeRates = new ArrayList<>();
        exchangeRates.add(new MonthlyExchangeRateDto(of(2021, 6, 1), Map.of("USD", valueOf(1))));
        when(exchangeRatesApiClient.getLastYearExchangeRates()).thenReturn(exchangeRates);
        when(monthlyExchangeRateRepository.findByDateIn(List.of(of(2021, 6, 1))))
                .thenReturn(List.of());

        fetchExchangeRatesService.fetchRateExchangesFromLastYear();

        final MonthlyExchangeRateEntity newExchangeRate = new MonthlyExchangeRateEntity();
        newExchangeRate.setDate(of(2021, 6, 1));
        newExchangeRate.setExchangeRates(Map.of("USD", valueOf(1)));
        verify(monthlyExchangeRateRepository).save(eq(newExchangeRate));
    }

    @DisplayName("when there are few new exchange rates then should add only them into DB")
    @Test
    void addOnlyNewExchangeRates() {
        final List<MonthlyExchangeRateDto> exchangeRates = new ArrayList<>();
        exchangeRates.add(new MonthlyExchangeRateDto(of(2021, 6, 1), Map.of("USD", valueOf(1))));
        exchangeRates.add(new MonthlyExchangeRateDto(of(2021, 7, 1), Map.of("USD", valueOf(2))));
        when(exchangeRatesApiClient.getLastYearExchangeRates()).thenReturn(exchangeRates);
        final MonthlyExchangeRateEntity entity = new MonthlyExchangeRateEntity();
        entity.setDate(of(2021, 6, 1));
        entity.setExchangeRates(Map.of("USD", valueOf(1)));
        when(monthlyExchangeRateRepository.findByDateIn(List.of(of(2021, 6, 1), of(2021, 7, 1))))
                .thenReturn(List.of(entity));

        fetchExchangeRatesService.fetchRateExchangesFromLastYear();

        final MonthlyExchangeRateEntity newExchangeRate = new MonthlyExchangeRateEntity();
        newExchangeRate.setDate(of(2021, 7, 1));
        newExchangeRate.setExchangeRates(Map.of("USD", valueOf(2)));
        verify(monthlyExchangeRateRepository).save(eq(newExchangeRate));
    }
}