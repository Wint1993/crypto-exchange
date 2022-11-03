package crypto.exchange.currency.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CryptocurrencyDTO {

    private BigDecimal rate;

    private BigDecimal amount;

    private BigDecimal result;

    private BigDecimal fee;
}
