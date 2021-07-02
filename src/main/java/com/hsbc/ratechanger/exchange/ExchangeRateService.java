package com.hsbc.ratechanger.exchange;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateService {

    MonthlyExchangeRateDto findByDate(LocalDate date);

    List<MonthlyExchangeRateDto> findByDateBetween(LocalDate from, LocalDate to);
}
