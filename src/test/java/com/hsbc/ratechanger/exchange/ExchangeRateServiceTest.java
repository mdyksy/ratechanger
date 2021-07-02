package com.hsbc.ratechanger.exchange;

import com.hsbc.ratechanger.exception.ExchangeRateNotFoundException;
import com.hsbc.ratechanger.persistence.MonthlyExchangeRateEntity;
import com.hsbc.ratechanger.persistence.MonthlyExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.math.BigDecimal.valueOf;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private MonthlyExchangeRateRepository monthlyExchangeRateRepository;

    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        exchangeRateService = new ExchangeRateServiceImpl(monthlyExchangeRateRepository);
    }

    @DisplayName("when exchange rate date is present then should return rates")
    @Test
    void findByDateExistingExchangeRate() {
        final MonthlyExchangeRateEntity entity = new MonthlyExchangeRateEntity();
        entity.setDate(of(2020, 1, 1));
        entity.setExchangeRates(Map.of("USD", valueOf(0.1)));
        when(monthlyExchangeRateRepository.findByDate(of(2020, 1, 1)))
                .thenReturn(Optional.of(entity));

        final MonthlyExchangeRateDto dto = exchangeRateService.findByDate(of(2020, 1, 1));

        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(new MonthlyExchangeRateDto(of(2020, 1, 1), Map.of("USD", valueOf(0.1))));
    }

    @DisplayName("when exchange rate date is not present then should throw exception")
    @Test
    void throwExceptionWhenFindByDate() {
        when(monthlyExchangeRateRepository.findByDate(of(2020, 1, 1)))
                .thenReturn(Optional.empty());

        final ExchangeRateNotFoundException exception = catchThrowableOfType(
                () -> exchangeRateService.findByDate(of(2020, 1, 1)),
                ExchangeRateNotFoundException.class);

        assertThat(exception.getMessage()).isEqualTo("Not found exchange rate for date [2020-01-01]");
    }

    @DisplayName("when exchange rates are present then should return those")
    @Test
    void findByDateBetweenExistingExchangeRates() {
        final MonthlyExchangeRateEntity entity = new MonthlyExchangeRateEntity();
        entity.setDate(of(2020, 1, 1));
        entity.setExchangeRates(Map.of("USD", valueOf(0.1)));
        when(monthlyExchangeRateRepository.findByDateBetween(of(2020, 1, 1), of(2021, 1, 1)))
                .thenReturn(List.of(entity));

        final List<MonthlyExchangeRateDto> dto = exchangeRateService.findByDateBetween(of(2020, 1, 1), of(2021, 1, 1));

        assertThat(dto)
                .hasSize(1)
                .containsExactly(new MonthlyExchangeRateDto(of(2020, 1, 1), Map.of("USD", valueOf(0.1))));
    }

    @DisplayName("when exchange rates are not present then should return empty list")
    @Test
    void findByDateBetweenNonExistingExchangeRates() {
        when(monthlyExchangeRateRepository.findByDateBetween(of(2020, 1, 1), of(2021, 1, 1)))
                .thenReturn(List.of());

        final List<MonthlyExchangeRateDto> dto = exchangeRateService.findByDateBetween(of(2020, 1, 1), of(2021, 1, 1));

        assertThat(dto).isEmpty();
    }
}