package crypto.exchange.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CryptocurrencyRequestDTO {

    @JsonProperty(value = "from")
    private String fromCurrency;

    @JsonProperty(value = "to")
    private Set<String> toCurrencySet;

    private BigDecimal amount;
}
