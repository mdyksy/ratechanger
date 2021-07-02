package com.hsbc.ratechanger.persistence;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyExchangeRateRepository extends CrudRepository<MonthlyExchangeRateEntity, Long> {

    @EntityGraph(attributePaths = {"exchangeRates"})
    List<MonthlyExchangeRateEntity> findByDateBetween(LocalDate from, LocalDate to);

    @EntityGraph(attributePaths = {"exchangeRates"})
    Optional<MonthlyExchangeRateEntity> findByDate(LocalDate date);

    List<MonthlyExchangeRateEntity> findByDateIn(List<LocalDate> date);
}
