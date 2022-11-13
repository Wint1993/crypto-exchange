package crypto.exchange.calculator;

import java.math.BigDecimal;
import java.util.Map;

class CryptocurrencyQuotesResponseDTO extends AbstractCryptocurrencyResponseDTO<BigDecimal> {

    CryptocurrencyQuotesResponseDTO(String source, Map<String, BigDecimal> rates) {
        super(source, rates);
    }
}
