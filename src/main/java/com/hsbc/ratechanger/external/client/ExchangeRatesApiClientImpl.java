package com.hsbc.ratechanger.external.client;

import com.hsbc.ratechanger.commons.DateTimeProvider;
import com.hsbc.ratechanger.exception.ExchangeRateNotFoundException;
import com.hsbc.ratechanger.exchange.MonthlyExchangeRateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class ExchangeRatesApiClientImpl implements ExchangeRatesApiClient {

    private final RestTemplate restTemplate;
    private final ExchangeRatesApiUrlBuilder exchangeRatesApiUrlBuilder;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public List<MonthlyExchangeRateDto> getLastYearExchangeRates() {
        final LocalDate to = dateTimeProvider.dateNow().withDayOfMonth(1);
        final LocalDate from = to.minusYears(1);

        return getExchangeRatesBetween(from, to);
    }

    private List<MonthlyExchangeRateDto> getExchangeRatesBetween(LocalDate from, LocalDate to) {
        final List<GetExchangeRatesApiResponse> exchangeRates = getRateExchangesForGivenPeriod(from, to, new ArrayList<>());

        return exchangeRates.stream()
                .map(rspRate -> new MonthlyExchangeRateDto(rspRate.getDate(), rspRate.getRates()))
                .collect(Collectors.toList());
    }

    private List<GetExchangeRatesApiResponse> getRateExchangesForGivenPeriod(LocalDate from, LocalDate to, List<GetExchangeRatesApiResponse> exchangeRates) {
        if (from.isAfter(to) || from.isEqual(to) || from.isAfter(dateTimeProvider.dateNow())) {
            return exchangeRates;
        }

        try {
            final ResponseEntity<GetExchangeRatesApiResponse> rsp = restTemplate.getForEntity(
                    exchangeRatesApiUrlBuilder.buildGetExchangeRatesByDateUrl(from),
                    GetExchangeRatesApiResponse.class);

            if (rsp.getStatusCode() == HttpStatus.OK && rsp.getBody() != null && rsp.getBody().isSuccess()) {
                exchangeRates.add(rsp.getBody());
            }
        } catch (HttpClientErrorException.BadRequest ex) {
            throw new ExchangeRateNotFoundException(from);
        }

        return getRateExchangesForGivenPeriod(from.plusMonths(1), to, exchangeRates);
    }
}
