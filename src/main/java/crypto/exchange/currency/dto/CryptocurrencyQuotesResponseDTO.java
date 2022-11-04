package crypto.exchange.currency.dto;

import java.math.BigDecimal;
import java.util.Map;

public class CryptocurrencyQuotesResponseDTO extends CryptocurrencyResponseDTO<BigDecimal> {

    public CryptocurrencyQuotesResponseDTO(String source, Map<String, BigDecimal> rates) {
        super(source, rates);
    }
}
