package org.example.currencyrateservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("fiat_rates")
@Data
public class FiatRate {

    @Id
    private Long id;

    @Column("currency")
    private String currency;

    @Column("rate")
    private BigDecimal rate;

    @Column("timestamp")
    private LocalDateTime timestamp;

}
