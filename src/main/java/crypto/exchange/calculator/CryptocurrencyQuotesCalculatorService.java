package crypto.exchange.calculator;

import crypto.exchange.rates.CryptoRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Service
class CryptocurrencyQuotesCalculatorService extends AbstractCryptocurrencyCalculatorService<CryptocurrencyQuotesResponseDTO, BigDecimal> {

    @Autowired
    CryptocurrencyQuotesCalculatorService(CryptoRatesService cryptoRatesService) {
        super(cryptoRatesService);
    }

    @Override
    protected CryptocurrencyQuotesResponseDTO createResult(String currency, Map<String, BigDecimal> currencyMap) {
        return new CryptocurrencyQuotesResponseDTO(currency, currencyMap);
    }

    @Override
    public CryptocurrencyQuotesResponseDTO calculate(String currency, Set<String> filters, BigDecimal amount) {
        return calculateCryptocurrency(currency, filters, amount);
    }

    @Override
    protected BigDecimal returnCurrencyResult(BigDecimal rate, BigDecimal amount) {
        return rate;
    }
}
