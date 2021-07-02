package com.hsbc.ratechanger.persistence;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "monthly_exchange_rates")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class MonthlyExchangeRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "monthly_exchange_rates_generator")
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ElementCollection
    @CollectionTable(
            name = "exchange_rates",
            joinColumns = {
                    @JoinColumn(name = "monthly_exchange_rate_id", referencedColumnName = "id")
            }
    )
    @MapKeyColumn(name = "currency")
    @Column(name = "rate", precision = 8, scale = 7)
    private Map<String, BigDecimal> exchangeRates = new HashMap<>();
}
