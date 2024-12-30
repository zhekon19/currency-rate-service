package org.example.currencyrateservice.service;


import org.example.currencyrateservice.dto.CryptoRateDto;
import org.example.currencyrateservice.dto.FiatRateDto;
import org.example.currencyrateservice.model.CryptoRate;
import org.example.currencyrateservice.model.CurrencyRateResponse;
import org.example.currencyrateservice.model.FiatRate;
import org.example.currencyrateservice.repository.CryptoRateRepository;
import org.example.currencyrateservice.repository.FiatRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class CurrencyRateService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyRateService.class);

    private final FiatRateRepository fiatRateRepository;
    private final CryptoRateRepository cryptoRateRepository;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public CurrencyRateService(FiatRateRepository fiatRateRepository, CryptoRateRepository cryptoRateRepository, WebClient.Builder webClientBuilder) {
        this.fiatRateRepository = fiatRateRepository;
        this.cryptoRateRepository = cryptoRateRepository;
        this.webClientBuilder = webClientBuilder;
    }

    @Value("${api.key}")
    private String apiKey;

    @Value("${fiat.api.url}")
    private String fiatApiUrl;

    @Value("${crypto.api.url}")
    private String cryptoApiUrl;

    public Mono<List<FiatRate>> fetchFiatRates() {
        logger.info("Fetching fiat rates from API: {}", fiatApiUrl);

        return webClientBuilder.build()
                .get()
                .uri(fiatApiUrl)
                .header("X-API-KEY", apiKey)
                .retrieve()
                .bodyToFlux(FiatRate.class)
                .doOnNext(fiatRate -> {
                    logger.debug("Received fiat rate: {} - {}", fiatRate.getCurrency(), fiatRate.getRate());
                })
                .collectList()
                .flatMap(fiatRates -> saveFiatRates(fiatRates).thenReturn(fiatRates))
                .onErrorResume(e -> {
                    logger.error("Error fetching fiat rates from API, falling back to DB: {}", e.getMessage());
                    return getLatestUniqueFiatRatesFromDb().collectList();
                });
    }


    public Mono<List<CryptoRate>> fetchCryptoRates() {
        logger.info("Fetching crypto rates from API: {}", cryptoApiUrl);

        return webClientBuilder.build()
                .get()
                .uri(cryptoApiUrl)
                .header("X-API-KEY", apiKey)
                .retrieve()
                .bodyToFlux(CryptoRate.class)
                .doOnNext(cryptoRate -> {
                    logger.debug("Received crypto rate: {} - {}", cryptoRate.getName(), cryptoRate.getValue());
                })
                .collectList()
                .flatMap(cryptoRates -> saveCryptoRates(cryptoRates).thenReturn(cryptoRates))
                .onErrorResume(e -> {
                    logger.error("Error fetching crypto rates from API, falling back to DB: {}", e.getMessage());
                    return getLatestUniqueCryptoRatesFromDb().collectList();
                });
    }


    public Flux<FiatRate> getLatestUniqueFiatRatesFromDb() {
        logger.info("Fetching latest unique fiat rates from DB...");
        return fiatRateRepository.findLatestUniqueRates();
    }

    public Flux<CryptoRate> getLatestUniqueCryptoRatesFromDb() {
        logger.info("Fetching latest unique crypto rates from DB...");
        return cryptoRateRepository.findLatestUniqueRates();
    }

    public Mono<Void> saveFiatRates(List<FiatRate> fiatRates) {
        if (fiatRates != null && !fiatRates.isEmpty()) {
            logger.info("Saving fiat rates to DB...");
            return Flux.fromIterable(fiatRates)
                    .doOnNext(fiatRate -> logger.debug("Saving fiat rate: {} - {}", fiatRate.getCurrency(), fiatRate.getRate()))
                    .flatMap(fiatRateRepository::save)
                    .then();
        }
        return Mono.empty();
    }

    public Mono<Void> saveCryptoRates(List<CryptoRate> cryptoRates) {
        if (cryptoRates != null && !cryptoRates.isEmpty()) {
            logger.info("Saving crypto rates to DB...");
            return Flux.fromIterable(cryptoRates)
                    .doOnNext(cryptoRate -> logger.debug("Saving crypto rate: {} - {}", cryptoRate.getName(), cryptoRate.getValue()))
                    .flatMap(cryptoRateRepository::save)
                    .then();
        }
        return Mono.empty();
    }

    public Mono<CurrencyRateResponse> getAggregatedCurrencyRates() {
        logger.info("Aggregating currency rates...");

        Mono<List<FiatRateDto>> fiatRatesDtoMono = fetchFiatRates()
                .flatMapMany(Flux::fromIterable)
                .map(this::toDto)
                .collectList();

        Mono<List<CryptoRateDto>> cryptoRatesDtoMono = fetchCryptoRates()
                .flatMapMany(Flux::fromIterable)
                .map(this::toDto)
                .collectList();

        return Mono.zip(fiatRatesDtoMono, cryptoRatesDtoMono)
                .map(tuple -> new CurrencyRateResponse(tuple.getT1(), tuple.getT2()));
    }

    private FiatRateDto toDto(FiatRate fiatRate) {
        return new FiatRateDto(fiatRate.getCurrency(), fiatRate.getRate());
    }

    private CryptoRateDto toDto(CryptoRate cryptoRate) {
        return new CryptoRateDto(cryptoRate.getName(), cryptoRate.getValue());
    }
}
