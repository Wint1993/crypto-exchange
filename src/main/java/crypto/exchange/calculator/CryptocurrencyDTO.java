package crypto.exchange.calculator;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
class CryptocurrencyDTO {

    private BigDecimal rate;

    private BigDecimal amount;

    private BigDecimal result;

    private BigDecimal fee;
}
