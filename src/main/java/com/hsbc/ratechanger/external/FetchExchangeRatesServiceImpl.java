package com.hsbc.ratechanger.external;

import com.hsbc.ratechanger.exchange.MonthlyExchangeRateDto;
import com.hsbc.ratechanger.external.client.ExchangeRatesApiClient;
import com.hsbc.ratechanger.persistence.MonthlyExchangeRateEntity;
import com.hsbc.ratechanger.persistence.MonthlyExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class FetchExchangeRatesServiceImpl implements FetchExchangeRatesService {

    private final ExchangeRatesApiClient exchangeRatesApiClient;
    private final MonthlyExchangeRateRepository monthlyExchangeRateRepository;

    @Override
    public void fetchRateExchangesFromLastYear() {
        final List<MonthlyExchangeRateDto> exchangeRates = exchangeRatesApiClient.getLastYearExchangeRates();

        final Set<LocalDate> existingExchangeRates = monthlyExchangeRateRepository.findByDateIn(
                exchangeRates.stream()
                        .map(MonthlyExchangeRateDto::getDate)
                        .collect(Collectors.toList()))
                .stream()
                .map(MonthlyExchangeRateEntity::getDate)
                .collect(Collectors.toSet());

        exchangeRates.removeIf(dto -> existingExchangeRates.contains(dto.getDate()));
        exchangeRates.forEach(dto -> createNewMonthlyRateExchange(dto.getDate(), dto.getRates()));
    }

    private void createNewMonthlyRateExchange(LocalDate date, Map<String, BigDecimal> rates) {
        final MonthlyExchangeRateEntity exchangeEntity = new MonthlyExchangeRateEntity();
        exchangeEntity.setDate(date);
        exchangeEntity.setExchangeRates(rates);
        monthlyExchangeRateRepository.save(exchangeEntity);
    }
}
