package com.hsbc.ratechanger.exchange;

import com.hsbc.ratechanger.exception.ExchangeRateNotFoundException;
import com.hsbc.ratechanger.persistence.MonthlyExchangeRateEntity;
import com.hsbc.ratechanger.persistence.MonthlyExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ExchangeRateServiceImpl implements ExchangeRateService {

    private final MonthlyExchangeRateRepository monthlyExchangeRateRepository;

    @Override
    public MonthlyExchangeRateDto findByDate(LocalDate date) {
        return monthlyExchangeRateRepository.findByDate(date)
                .map(this::mapToDto)
                .orElseThrow(() -> new ExchangeRateNotFoundException(date));
    }

    @Override
    public List<MonthlyExchangeRateDto> findByDateBetween(LocalDate from, LocalDate to) {
        return monthlyExchangeRateRepository.findByDateBetween(from, to)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private MonthlyExchangeRateDto mapToDto(MonthlyExchangeRateEntity entity) {
        return new MonthlyExchangeRateDto(entity.getDate(), entity.getExchangeRates());
    }
}