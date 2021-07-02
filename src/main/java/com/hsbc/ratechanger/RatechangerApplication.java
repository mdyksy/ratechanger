package com.hsbc.ratechanger;

import com.hsbc.ratechanger.external.client.ExchangeRatesApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ExchangeRatesApiProperties.class})
public class RatechangerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RatechangerApplication.class, args);
    }
}
