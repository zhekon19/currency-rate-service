package org.example.currencyrateservice.repository;


import org.example.currencyrateservice.model.CryptoRate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CryptoRateRepository extends ReactiveCrudRepository<CryptoRate, Long> {
    @Query("SELECT DISTINCT ON (name) * FROM crypto_rates ORDER BY name, timestamp DESC")
    Flux<CryptoRate> findLatestUniqueRates();
}
