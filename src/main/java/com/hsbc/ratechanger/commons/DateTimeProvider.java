package com.hsbc.ratechanger.commons;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateTimeProvider {

    public LocalDate dateNow() {
        return LocalDate.now();
    }
}
