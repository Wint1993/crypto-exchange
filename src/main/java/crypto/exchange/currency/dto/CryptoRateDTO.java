package crypto.exchange.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CryptoRateDTO {

    @JsonProperty("asset_id_quote")
    private String currency;

    @JsonProperty("rate")
    private BigDecimal rate;
}
