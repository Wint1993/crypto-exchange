package crypto.exchange.currency.dto;

import java.util.Map;

public class CryptocurrencyRatesResponseDTO extends CryptocurrencyResponseDTO<CryptocurrencyDTO> {
    public CryptocurrencyRatesResponseDTO(String source, Map<String, CryptocurrencyDTO> rates) {
        super(source, rates);
    }
}
