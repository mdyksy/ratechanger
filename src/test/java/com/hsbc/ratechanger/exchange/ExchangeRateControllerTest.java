package com.hsbc.ratechanger.exchange;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("should return exchange rates from DB between given date and now")
    @Test
    @Sql(scripts = "classpath:db/init.sql")
    void findByDateFrom() throws Exception {
        mockMvc.perform(get("/exchange/rate/from/2021-01-01"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].date").value("2021-01-01"))
                .andExpect(jsonPath("$[0].rates.HKD").value("8.720366"))
                .andExpect(jsonPath("$[0].rates.GBP").value("0.902684"))
                .andExpect(jsonPath("$[0].rates.USD").value("1.1251"));
    }

    @DisplayName("should return exchange rate from DB for given date")
    @Test
    @Sql(scripts = "classpath:db/init.sql")
    void findByDate() throws Exception {
        mockMvc.perform(get("/exchange/rate/2021-01-01"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value("2021-01-01"))
                .andExpect(jsonPath("$.rates.HKD").value("8.720366"))
                .andExpect(jsonPath("$.rates.GBP").value("0.902684"))
                .andExpect(jsonPath("$.rates.USD").value("1.1251"));
    }

    @DisplayName("should throw exception when given exchange rate is not present in DB")
    @Test
    @Sql(scripts = "classpath:db/init.sql")
    void findByDateWhenNotExists() throws Exception {
        mockMvc.perform(get("/exchange/rate/2021-06-01"))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .string("Not found exchange rate for date [2021-06-01]"));
    }
}