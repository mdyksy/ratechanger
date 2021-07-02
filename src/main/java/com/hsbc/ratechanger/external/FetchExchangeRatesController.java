package com.hsbc.ratechanger.external;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "REST API related to fetching rate exchanges into DB")
@RestController
@RequestMapping("/exchange/rate")
@RequiredArgsConstructor
public class FetchExchangeRatesController {

    private final FetchExchangeRatesService fetchExchangeRatesService;

    @ApiOperation(value = "Fetch rate exchanges from external API into DB")
    @PostMapping
    public void fetchRateExchanges() {
        fetchExchangeRatesService.fetchRateExchangesFromLastYear();
    }
}
