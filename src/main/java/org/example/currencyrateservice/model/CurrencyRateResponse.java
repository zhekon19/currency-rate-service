package org.example.currencyrateservice.model;

import lombok.*;
import org.example.currencyrateservice.dto.CryptoRateDto;
import org.example.currencyrateservice.dto.FiatRateDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CurrencyRateResponse {

    private List<FiatRateDto> fiatRates;
    private List<CryptoRateDto> cryptoRates;

}
