package com.hsbc.ratechanger.external.client;

import com.hsbc.ratechanger.exchange.MonthlyExchangeRateDto;

import java.util.List;

public interface ExchangeRatesApiClient {

    List<MonthlyExchangeRateDto> getLastYearExchangeRates();
}
