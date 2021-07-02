package com.hsbc.ratechanger.exchange;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class MonthlyExchangeRateDto {

    private final LocalDate date;
    private final Map<String, BigDecimal> rates;
}
