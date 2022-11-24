package crypto.exchange.calculator;

import crypto.exchange.rates.CryptoRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Service
class CryptocurrencyQuotesCalculatorService extends AbstractCryptocurrencyCalculatorService<CryptocurrencyQuotesResponseDTO, BigDecimal> implements CalculatorTypeStrategy {

    @Autowired
    CryptocurrencyQuotesCalculatorService(CryptoRatesService cryptoRatesService) {
        super(cryptoRatesService);
    }

    @Override
    protected CryptocurrencyQuotesResponseDTO createResult(String currency, Map<String, BigDecimal> currencyMap) {
        return new CryptocurrencyQuotesResponseDTO(currency, currencyMap);
    }

    @Override
    protected BigDecimal returnCurrencyResult(BigDecimal rate, BigDecimal amount) {
        return rate;
    }

    @Override
    public CalculatorTypeEnum getCalculatorType() {
        return CalculatorTypeEnum.QUOTES;
    }

    @Override
    public CryptocurrencyQuotesResponseDTO calculateCrypto(String currency, Set<String> filters, BigDecimal amount) {
        return calculateCryptocurrency(currency, filters, amount);
    }
}
