package com.hsbc.ratechanger.external.client;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class GetExchangeRatesApiResponse {

    private boolean success;
    private LocalDate date;
    private Map<String, BigDecimal> rates;
}
