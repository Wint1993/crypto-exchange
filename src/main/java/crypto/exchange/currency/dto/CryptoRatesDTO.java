package crypto.exchange.currency.dto;

import lombok.Data;

import java.util.List;

@Data
public class CryptoRatesDTO {

    List<CryptoRateDTO> rates;
}
