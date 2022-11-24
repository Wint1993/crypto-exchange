package crypto.exchange.calculator;

import java.math.BigDecimal;
import java.util.Map;

final class CryptocurrencyQuotesResponseDTO extends AbstractCryptocurrencyResponseDTO<BigDecimal> {

    CryptocurrencyQuotesResponseDTO(String source, Map<String, BigDecimal> rates) {
        super(source, rates);
    }

}
