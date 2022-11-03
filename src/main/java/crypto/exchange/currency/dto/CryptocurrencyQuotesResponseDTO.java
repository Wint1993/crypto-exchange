package crypto.exchange.currency.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class CryptocurrencyQuotesResponseDTO {

    private String source;

    private Map<String, BigDecimal> rates;
}
