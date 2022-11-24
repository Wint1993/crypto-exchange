package crypto.exchange.calculator;

import java.util.Map;

final class CryptocurrencyForecastResponseDTO extends AbstractCryptocurrencyResponseDTO<CryptocurrencyDTO> {

    CryptocurrencyForecastResponseDTO(String source, Map<String, CryptocurrencyDTO> rates) {
        super(source, rates);
    }

}
