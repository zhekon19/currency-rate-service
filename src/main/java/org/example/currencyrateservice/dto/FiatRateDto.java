package org.example.currencyrateservice.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FiatRateDto {
    private String currency;
    private BigDecimal rate;
}