package org.example.currencyrateservice.controller;

import org.example.currencyrateservice.model.CurrencyRateResponse;
import org.example.currencyrateservice.service.CurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;


@RestController
@RequestMapping("/currency-rates")
public class CurrencyRateController {

    private final CurrencyRateService currencyRateService;

    @Autowired
    public CurrencyRateController(CurrencyRateService currencyRateService) {
        this.currencyRateService = currencyRateService;
    }
    @GetMapping
    public Mono<ResponseEntity<CurrencyRateResponse>> getAllRates() {
        return currencyRateService.getAggregatedCurrencyRates()
                .map(response -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response))
                .onErrorResume(error -> {
                    System.err.println("Error fetching currency rates: " + error.getMessage());
                    return Mono.just(ResponseEntity.ok(
                            new CurrencyRateResponse(Collections.emptyList(), Collections.emptyList())
                    ));
                });
    }

    @GetMapping("/hello")
    public String getString() {
        return "Hello World";
    }

}
