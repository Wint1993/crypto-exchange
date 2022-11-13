package crypto.exchange.calculator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
class CryptocurrencyRequestDTO {

    @JsonProperty(value = "from")
    private String currency;

    @JsonProperty(value = "to")
    private Set<String> filters;

    private BigDecimal amount;
}
