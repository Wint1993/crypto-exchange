package crypto.exchange.rates;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CryptoRateDTO {

    @JsonProperty("asset_id_quote")
    private String currency;

    @JsonProperty("rate")
    private BigDecimal rate;
}
