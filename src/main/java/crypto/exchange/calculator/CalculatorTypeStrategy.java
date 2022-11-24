package crypto.exchange.calculator;

import java.math.BigDecimal;
import java.util.Set;

interface CalculatorTypeStrategy {

    CalculatorTypeEnum getCalculatorType();

    AbstractCryptocurrencyResponseDTO calculateCrypto(String currency, Set<String> filters, BigDecimal amount);
}
