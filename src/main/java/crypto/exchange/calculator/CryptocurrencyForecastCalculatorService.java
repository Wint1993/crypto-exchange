package crypto.exchange.calculator;

import crypto.exchange.rates.CryptoRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Service
class CryptocurrencyForecastCalculatorService extends AbstractCryptocurrencyCalculatorService<CryptocurrencyForecastResponseDTO, CryptocurrencyDTO> implements CalculatorTypeStrategy {

    @Autowired
    CryptocurrencyForecastCalculatorService(CryptoRatesService cryptoRatesService) {
        super(cryptoRatesService);
    }

    @Override
    protected CryptocurrencyForecastResponseDTO createResult(String currency, Map<String, CryptocurrencyDTO> currencyMap) {
        return new CryptocurrencyForecastResponseDTO(currency, currencyMap);
    }

    @Override
    protected CryptocurrencyDTO returnCurrencyResult(BigDecimal rate, BigDecimal amount) {
        return CryptocurrencyDTO.builder()
                .rate(rate)
                .amount(amount)
                .result(amount.multiply(rate).add(amount.multiply(rate).multiply(FEE)))
                .fee(FEE)
                .build();
    }

    @Override
    public CalculatorTypeEnum getCalculatorType() {
        return CalculatorTypeEnum.FORECAST;
    }

    @Override
    public CryptocurrencyForecastResponseDTO calculateCrypto(String currency, Set<String> filters, BigDecimal amount) {
        return calculateCryptocurrency(currency, filters, amount);
    }

}
