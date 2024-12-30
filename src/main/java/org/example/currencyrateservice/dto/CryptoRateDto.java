package org.example.currencyrateservice.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CryptoRateDto {
    private String currency;
    private BigDecimal rate;

}