package com.hsbc.ratechanger.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsbc.ratechanger.SpringTestConfig;
import com.hsbc.ratechanger.external.client.GetExchangeRatesApiResponse;
import com.hsbc.ratechanger.persistence.MonthlyExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {SpringTestConfig.class})
@ActiveProfiles("test")
class FetchExchangeRatesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MonthlyExchangeRateRepository monthlyExchangeRateRepository;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @DisplayName("when sending correct request then should add exchange rates from last year to DB")
    @Test
    void fetchDataFromExternalApi() throws Exception {
        final GetExchangeRatesApiResponse response = new GetExchangeRatesApiResponse();
        response.setSuccess(true);
        response.setDate(LocalDate.of(2021, 1, 1));
        response.setRates(Map.of("USD", BigDecimal.valueOf(1.1251)));
        mockServer.expect(ExpectedCount.times(12),
                requestTo(matchesPattern("http://api.exchangeratesapi.io/v1/.*")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(response)));

        mockMvc.perform(post("/exchange/rate"))
                .andExpect(status().isOk());

        assertThat(monthlyExchangeRateRepository.findAll()).hasSize(12);
    }

    @DisplayName("when external API returns 404 then should handle it and return NOT_FOUND")
    @Test
    void return404whenExternalApiHasError() throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(matchesPattern("http://api.exchangeratesapi.io/v1/.*")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        mockMvc.perform(post("/exchange/rate"))
                .andExpect(status().isNotFound());
    }
}