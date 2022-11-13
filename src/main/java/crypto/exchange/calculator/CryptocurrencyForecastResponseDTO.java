package crypto.exchange.calculator;

import java.util.Map;

class CryptocurrencyForecastResponseDTO extends AbstractCryptocurrencyResponseDTO<CryptocurrencyDTO> {

    CryptocurrencyForecastResponseDTO(String source, Map<String, CryptocurrencyDTO> rates) {
        super(source, rates);
    }
}
