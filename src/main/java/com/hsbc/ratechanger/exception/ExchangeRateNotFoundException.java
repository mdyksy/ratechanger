package com.hsbc.ratechanger.exception;

import java.time.LocalDate;

import static java.lang.String.format;

public class ExchangeRateNotFoundException extends RuntimeException {

    public ExchangeRateNotFoundException(LocalDate date) {
        super(format("Not found exchange rate for date [%s]", date.toString()));
    }
}
