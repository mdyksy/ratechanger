package com.hsbc.ratechanger.exchange;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Api(tags = "REST API related to fetching rate exchanges")
@RestController
@RequestMapping("/exchange/rate")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @ApiOperation(value = "Get rate exchanges between given date and now")
    @GetMapping("/from/{date}")
    public List<MonthlyExchangeRateDto> findByDateFrom(
            @ApiParam(required = true, defaultValue = "2021-01-01") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return exchangeRateService.findByDateBetween(date, LocalDate.now());
    }

    @ApiOperation(value = "Get rate exchanges by date")
    @GetMapping("/{date}")
    public MonthlyExchangeRateDto findByDate(
            @ApiParam(required = true, defaultValue = "2021-06-01") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return exchangeRateService.findByDate(date);
    }
}
