package org.example.currencyrateservice.repository;


import org.example.currencyrateservice.model.FiatRate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface FiatRateRepository extends ReactiveCrudRepository<FiatRate, Long> {
    @Query("SELECT DISTINCT ON (currency) * FROM fiat_rates ORDER BY currency, timestamp DESC")
    Flux<FiatRate> findLatestUniqueRates();
}
