package org.example.currencyrateservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("crypto_rates")
@Data
public class CryptoRate {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("value")
    private BigDecimal value;

    @Column("timestamp")
    private LocalDateTime timestamp;

}
